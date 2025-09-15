package com.epam.learn.trsessionservice.application.usecase;

import com.epam.learn.trsessionservice.adapter.api.rest.dto.TrainerTrainingSessionsReport;
import com.epam.learn.trsessionservice.domain.model.TrainingSession;
import com.epam.learn.trsessionservice.domain.repository.TrainingSessionRepository;
import com.epam.learn.trsessionservice.domain.service.TrainingSessionReportBuilder;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class SessionReportBuilder implements TrainingSessionReportBuilder {

  private final TrainingSessionRepository repository;

  @Override
  public TrainerTrainingSessionsReport build(String trainerUsername) {
    List<TrainingSession> trainingSessions = repository.findAllByTrainerUsername(trainerUsername);
    if (trainingSessions.isEmpty()) {
      throw new RuntimeException("No training sessions found for trainer: " + trainerUsername);
    }
    return TrainerTrainingSessionsReport.of(
        trainingSessions.getFirst(), calcWorkloadSummary(trainingSessions));
  }

  private List<TrainerTrainingSessionsReport.Year> calcWorkloadSummary(
      List<TrainingSession> sessions) {
    return sessions.stream()
        .collect(
            Collectors.groupingBy(
                s -> s.getTrainingDate().getYear(),
                Collectors.groupingBy(s -> s.getTrainingDate().getMonthValue())))
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByKey())
        .map(
            yearEntry -> {
              List<TrainerTrainingSessionsReport.Month> months =
                  yearEntry.getValue().entrySet().stream()
                      .sorted(Map.Entry.comparingByKey())
                      .map(
                          monthEntry -> {
                            long totalMinutes =
                                monthEntry.getValue().stream()
                                    .map(TrainingSession::getTrainingDuration)
                                    .mapToLong(t -> t.getHour() * 60L + t.getMinute())
                                    .sum();
                            String workingHours =
                                "%02d:%02d".formatted(totalMinutes / 60, totalMinutes % 60);
                            Month m = Month.of(monthEntry.getKey());
                            return new TrainerTrainingSessionsReport.Month(
                                m.getValue(), m.name(), workingHours);
                          })
                      .toList();

              return new TrainerTrainingSessionsReport.Year(yearEntry.getKey(), months);
            })
        .toList();
  }
}
