package com.epam.learn.gymservice.training.adapter.api.rest.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record TrainingSessionEvent(
    Long id,
    String trainerUsername,
    String trainerFirstName,
    String trainerLastName,
    Boolean isActive,
    LocalDate trainingDate,
    LocalTime trainingDuration,
    ActionType actionType) {

  public enum ActionType {
    ADD,
    DELETE
  }
}
