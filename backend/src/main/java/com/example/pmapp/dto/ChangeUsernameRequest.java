package com.example.pmapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeUsernameRequest(
        @NotBlank String currentPassword,
        @NotBlank @Size(min = 3, max = 50) String newUsername
) {}
