package com.epam.learn.trsessionservice.application.usecase;

import com.epam.learn.trsessionservice.adapter.api.rest.dto.TrainerTrainingSessionsReport;
import com.epam.learn.trsessionservice.domain.model.TrainingSession;
import com.epam.learn.trsessionservice.domain.repository.TrainingSessionRepository;
import com.epam.learn.trsessionservice.domain.service.TrainingSessionReportBuilder;
import java.time.LocalTime;
import java.util.List;
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
    TrainingSession session = repository.getByTrainerUsername(trainerUsername);
    if (session.getYears().isEmpty()) {
      throw new RuntimeException("No training sessions found for trainer: " + trainerUsername);
    }
    List<TrainerTrainingSessionsReport.Year> years =
        session.getYears().stream()
            .map(
                y ->
                    new TrainerTrainingSessionsReport.Year(
                        y.getYear(),
                        y.getMonths().stream()
                            .map(
                                m ->
                                    new TrainerTrainingSessionsReport.Month(
                                        m.getMonth(), LocalTime.ofSecondOfDay(m.getWorkload())))
                            .toList()))
            .toList();
    return TrainerTrainingSessionsReport.of(session, years);
  }
}
