package com.epam.learn.gymservice.trainee.adapter.api.rest.dto;

import java.time.LocalDate;

public record TraineeTrainingResponse(
    String name,
    LocalDate trainingDate,
    String duration,
    String trainingType,
    String trainerName) {}
