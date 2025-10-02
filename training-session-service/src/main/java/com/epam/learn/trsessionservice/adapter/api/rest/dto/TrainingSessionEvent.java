package com.epam.learn.trsessionservice.adapter.api.rest.dto;

import com.epam.learn.trsessionservice.domain.model.ActionType;
import com.epam.learn.trsessionservice.infra.validation.EnumNamePattern;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalTime;

public record TrainingSessionEvent(
    @NotNull @Positive Long id,
    @NotBlank String trainerUsername,
    @NotBlank String trainerFirstName,
    @NotBlank String trainerLastName,
    @NotNull Boolean trainerStatus,
    @NotNull @FutureOrPresent LocalDate trainingDate,
    @NotNull LocalTime trainingDuration,
    @NotNull @EnumNamePattern(regexp = "ADD|DELETE") ActionType actionType) {}
