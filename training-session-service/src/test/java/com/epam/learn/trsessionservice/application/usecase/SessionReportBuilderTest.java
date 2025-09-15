package com.epam.learn.trsessionservice.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;

import com.epam.learn.trsessionservice.adapter.api.rest.dto.TrainerTrainingSessionsReport;
import com.epam.learn.trsessionservice.domain.model.TrainingSession;
import com.epam.learn.trsessionservice.domain.repository.TrainingSessionRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SessionReportBuilderTest {

  public static final String TRAINER_USERNAME = "John.Doe";

  @Mock private TrainingSessionRepository repository;

  @InjectMocks private SessionReportBuilder sessionReportBuilder;

  @Test
  @DisplayName("<build> should properly build trainer's session report")
  void test_build_shouldBuildTrainerSessionReport() {
    List<TrainingSession> sessions =
        List.of(
            commonBuilder()
                .id(1L)
                .trainingDate(LocalDate.of(2025, 1, 10))
                .trainingDuration(LocalTime.of(1, 10))
                .build(),
            commonBuilder()
                .id(2L)
                .trainingDate(LocalDate.of(2025, 1, 20))
                .trainingDuration(LocalTime.of(0, 45))
                .build(),
            commonBuilder()
                .id(3L)
                .trainingDate(LocalDate.of(2025, 2, 5))
                .trainingDuration(LocalTime.of(10, 30))
                .build());

    Mockito.when(repository.findAllByTrainerUsername(anyString())).thenReturn(sessions);

    TrainerTrainingSessionsReport report =
        assertDoesNotThrow(() -> sessionReportBuilder.build(TRAINER_USERNAME));

    assertAll(
        () -> assertThat(report.trainerUsername()).isEqualTo(TRAINER_USERNAME),
        () -> assertThat(report.trainerFirstName()).isEqualTo("John"),
        () -> assertThat(report.trainerLastName()).isEqualTo("Doe"),
        () -> assertThat(report.trainerStatus()).isTrue(),
        () -> assertThat(report.workloadSummary()).hasSize(1));

    TrainerTrainingSessionsReport.Year summaryOfYear = report.workloadSummary().getFirst();
    assertThat(summaryOfYear.year()).isEqualTo(2025);

    assertThat(summaryOfYear.months())
        .extracting("number", "name", "workloadHours")
        .containsExactly(
            tuple(Month.JANUARY.getValue(), Month.JANUARY.toString(), "01:55"),
            tuple(Month.FEBRUARY.getValue(), Month.FEBRUARY.toString(), "10:30"));

    Mockito.verify(repository).findAllByTrainerUsername(TRAINER_USERNAME);
  }

  private TrainingSession.TrainingSessionBuilder commonBuilder() {
    return TrainingSession.builder()
        .trainerUsername(TRAINER_USERNAME)
        .trainerFirstName("John")
        .trainerLastName("Doe")
        .isActive(true);
  }
}
