package com.epam.learn.gymservice.trainer.adapter.api.rest.dto;

import com.epam.learn.gymcrm.infrastructure.mapper.UserMappings;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TrainerCreateRequest(
    @NotBlank String firstName, @NotBlank String lastName, @NotNull @Positive Long trainingTypeId)
    implements UserMappings {}
