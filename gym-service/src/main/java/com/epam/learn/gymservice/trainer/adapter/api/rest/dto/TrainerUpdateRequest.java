package com.epam.learn.gymservice.trainer.adapter.api.rest.dto;

import com.epam.learn.gymservice.infra.mapper.UserUpdateMappings;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TrainerUpdateRequest(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotNull Boolean isActive,
    @NotNull @Positive Long specializationRef)
    implements UserUpdateMappings {}
