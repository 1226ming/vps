package com.example.vps.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordRequest(@NotBlank String oldPassword, @NotBlank String newPassword) {
}
