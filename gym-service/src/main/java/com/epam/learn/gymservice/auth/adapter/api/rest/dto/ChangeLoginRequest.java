package com.epam.learn.gymservice.auth.adapter.api.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeLoginRequest(
    @NotBlank String username, @NotBlank String oldPassword, @NotBlank String newPassword) {}
