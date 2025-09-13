package com.epam.learn.gymservice.trainee.adapter.api.rest.dto;

import com.epam.learn.gymservice.infra.mapper.UserUpdateMappings;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public record TraineeUpdateRequest(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @Past LocalDate birthDate,
    String address,
    @NotNull Boolean isActive)
    implements UserUpdateMappings {}
