package com.epam.learn.gymservice.trainer.adapter.api.rest.dto;

import java.time.LocalDate;

public record TrainerTrainingResponse(
    String name,
    LocalDate trainingDate,
    String duration,
    String trainingType,
    String traineeName) {}
