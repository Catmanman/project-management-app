package com.example.pmapp.service;

import com.example.pmapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    @Transactional
    public void deleteByUsername(String username) {
        var u = users.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        users.deleteById(u.getId());
    }

    @Transactional
    public void deleteByIdAsAdmin(Integer id) {
        if (!users.existsById(id)) {
            // idempotent delete: treat as success if already gone
            return;
        }
        users.deleteById(id);
    }
}
