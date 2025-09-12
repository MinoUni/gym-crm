package com.epam.learn.gymservice.auth.adapter.api.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthCredentials(@NotBlank String username, @NotBlank String password) {}
