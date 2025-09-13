package com.epam.learn.gymservice.training.adapter.api.rest.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalTime;

public record TrainingCreateRequest(
    @NotBlank String name,
    @NotNull @FutureOrPresent LocalDate date,
    @NotNull LocalTime duration,
    @NotNull @Positive Long trainingTypeId,
    @NotBlank String trainerUsername,
    @NotBlank String traineeUsername) {}
