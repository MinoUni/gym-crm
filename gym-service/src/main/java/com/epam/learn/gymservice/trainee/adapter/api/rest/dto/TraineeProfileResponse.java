package com.epam.learn.gymservice.trainee.adapter.api.rest.dto;

import java.time.LocalDate;
import java.util.List;

public record TraineeProfileResponse(
    String username,
    String firstName,
    String lastName,
    LocalDate birthDate,
    String address,
    Boolean isActive,
    List<TrainerProfile> trainers) {

  public record TrainerProfile(
      String username, String firstName, String lastName, String specialization) {}
}
