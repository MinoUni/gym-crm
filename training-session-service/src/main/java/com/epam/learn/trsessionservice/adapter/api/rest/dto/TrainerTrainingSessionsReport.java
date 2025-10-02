package com.epam.learn.trsessionservice.adapter.api.rest.dto;

import com.epam.learn.trsessionservice.domain.model.TrainingSession;
import java.time.LocalTime;
import java.util.List;

public record TrainerTrainingSessionsReport(
    String trainerUsername,
    String trainerFirstName,
    String trainerLastName,
    Boolean trainerStatus,
    List<Year> workloadSummary) {

  public record Year(Integer year, List<Month> months) {}

  public record Month(String month, LocalTime workloadHours) {}

  public static TrainerTrainingSessionsReport of(
      TrainingSession trainerDetails, List<Year> workloadSummary) {
    return new TrainerTrainingSessionsReport(
        trainerDetails.getTrainerUsername(),
        trainerDetails.getTrainerFirstName(),
        trainerDetails.getTrainerLastName(),
        trainerDetails.getTrainerStatus(),
        workloadSummary);
  }
}
