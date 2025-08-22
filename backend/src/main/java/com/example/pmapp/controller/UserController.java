package com.example.pmapp.controller;

import com.example.pmapp.dto.ChangePasswordRequest;
import com.example.pmapp.dto.ChangeUsernameRequest;
import com.example.pmapp.model.User;
import com.example.pmapp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public UserController(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    // ------------ change USERNAME (requires current password) ------------
    @PutMapping("/me/username")
    @Transactional
    public ResponseEntity<Void> changeUsername(
            Authentication auth,
            @RequestBody ChangeUsernameRequest req) {

        final String username = Objects.requireNonNull(auth.getName(), "No principal");
        User u = users.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        // 1) verify current password
        if (!encoder.matches(getCurrentPassword(req), u.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current password is incorrect");
        }

        final String newUsername = getNewUsername(req).trim();
        if (newUsername.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New username required");
        }
        if (!newUsername.equals(u.getUsername()) && users.existsByUsername(newUsername)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already in use");
        }

        u.setUsername(newUsername);
        users.save(u);
        return ResponseEntity.noContent().build();
    }

    // ------------ change PASSWORD (requires current password) ------------
    @PutMapping("/me/password")
    @Transactional
    public ResponseEntity<Void> changePassword(
            Authentication auth,
            @RequestBody ChangePasswordRequest req) {

        final String username = Objects.requireNonNull(auth.getName(), "No principal");
        User u = users.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        // 1) verify current password
        if (!encoder.matches(getCurrentPassword(req), u.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current password is incorrect");
        }

        final String newPassword = getNewPassword(req);
        if (newPassword == null || newPassword.length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters");
        }

        // 2) update password
        u.setPasswordHash(encoder.encode(newPassword));
        users.save(u);

        // Optional: invalidate sessions/tokens here if you keep a token store.
        return ResponseEntity.noContent().build();
    }

    // ------- helpers to support either record- or POJO-style DTOs -------
    private static String getCurrentPassword(ChangeUsernameRequest r) {
        try { return (String) ChangeUsernameRequest.class.getMethod("getCurrentPassword").invoke(r); }
        catch (Exception ignore) { /* try record accessor */ }
        try { return (String) ChangeUsernameRequest.class.getMethod("currentPassword").invoke(r); }
        catch (Exception e) { throw new IllegalArgumentException("currentPassword missing"); }
    }

    private static String getNewUsername(ChangeUsernameRequest r) {
        try { return (String) ChangeUsernameRequest.class.getMethod("getNewUsername").invoke(r); }
        catch (Exception ignore) { }
        try { return (String) ChangeUsernameRequest.class.getMethod("newUsername").invoke(r); }
        catch (Exception e) { throw new IllegalArgumentException("newUsername missing"); }
    }

    private static String getCurrentPassword(ChangePasswordRequest r) {
        try { return (String) ChangePasswordRequest.class.getMethod("getCurrentPassword").invoke(r); }
        catch (Exception ignore) { }
        try { return (String) ChangePasswordRequest.class.getMethod("currentPassword").invoke(r); }
        catch (Exception e) { throw new IllegalArgumentException("currentPassword missing"); }
    }

    private static String getNewPassword(ChangePasswordRequest r) {
        try { return (String) ChangePasswordRequest.class.getMethod("getNewPassword").invoke(r); }
        catch (Exception ignore) { }
        try { return (String) ChangePasswordRequest.class.getMethod("newPassword").invoke(r); }
        catch (Exception e) { throw new IllegalArgumentException("newPassword missing"); }
    }
}
