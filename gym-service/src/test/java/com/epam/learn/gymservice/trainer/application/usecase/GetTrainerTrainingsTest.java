package com.epam.learn.gymservice.trainer.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.trainer.adapter.api.rest.dto.TrainerTrainingResponse;
import com.epam.learn.gymservice.trainer.domain.repository.TrainerRepository;
import com.epam.learn.gymservice.training.adapter.spi.persistence.TrainingMapper;
import com.epam.learn.gymservice.training.domain.model.Training;
import com.epam.learn.gymservice.training.domain.repository.TrainingRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class GetTrainerTrainingsTest {

  @Mock private TrainingMapper mapper;
  @Mock private TrainerRepository trainerRepository;
  @Mock private TrainingRepository trainingRepository;

  @InjectMocks private GetTrainerTrainings getTrainerTrainings;

  @Test
  @DisplayName("<execute> should throw <EntityNotFoundException>")
  void test_execute_shouldThrowEntityNotFoundException() {
    String username = "Mark.Chroma";

    when(trainerRepository.existsByUsername(anyString())).thenReturn(false);

    var e =
        assertThrows(
            EntityNotFoundException.class,
            () -> getTrainerTrainings.execute(username, null, null, null));

    assertNotNull(e);
    assertEquals(
        e.getMessage(), "Unable to find <Trainer> profile with username='%s'".formatted(username));

    verify(trainerRepository).existsByUsername(username);
    verify(trainingRepository, never()).findAll(ArgumentMatchers.<Specification<Training>>any());
    verify(mapper, never()).toTraineeTrainingResponse(isA(Training.class));
  }

  @Test
  @DisplayName("<execute> should get trainer trainings and map them to DTO")
  void test_execute_shouldGetTrainingsAndMapToResponse() {
    String username = "Gregor.Busk";
    LocalDate date = LocalDate.now();
    Training training = new Training();
    TrainerTrainingResponse resp =
        new TrainerTrainingResponse("Tr-1", date, "00:30:00", "type", "John Johnson");

    when(trainerRepository.existsByUsername(anyString())).thenReturn(true);
    when(trainingRepository.findAll(ArgumentMatchers.<Specification<Training>>any()))
        .thenReturn(List.of(training));
    when(mapper.toTrainerTrainingResponse(any(Training.class))).thenReturn(resp);

    assertDoesNotThrow(() -> getTrainerTrainings.execute(username, date, date, resp.traineeName()));

    verify(trainerRepository).existsByUsername(username);
    verify(trainingRepository).findAll(ArgumentMatchers.<Specification<Training>>any());
    verify(mapper).toTrainerTrainingResponse(training);
  }
}
