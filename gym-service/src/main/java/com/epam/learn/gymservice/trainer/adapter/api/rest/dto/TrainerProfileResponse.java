package com.epam.learn.gymservice.trainer.adapter.api.rest.dto;

import java.util.List;

public record TrainerProfileResponse(
    String firstName,
    String lastName,
    boolean isActive,
    Specialization specialization,
    List<TraineeProfile> trainees) {

  public record Specialization(Long id, String name) {}

  public record TraineeProfile(String username, String firstName, String lastName) {}
}
