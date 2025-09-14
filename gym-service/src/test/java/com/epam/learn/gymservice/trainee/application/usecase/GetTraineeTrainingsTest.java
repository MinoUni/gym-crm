package com.epam.learn.gymservice.trainee.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.trainee.adapter.api.rest.dto.TraineeTrainingResponse;
import com.epam.learn.gymservice.trainee.domain.repository.TraineeRepository;
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
class GetTraineeTrainingsTest {

  @Mock private TrainingMapper mapper;
  @Mock private TraineeRepository traineeRepository;
  @Mock private TrainingRepository trainingRepository;

  @InjectMocks private GetTraineeTrainings getTraineeTrainings;

  @Test
  @DisplayName("<execute> should throw <EntityNotFoundException>")
  void test_execute_shouldThrowEntityNotFoundException() {
    String username = "Mark.Chroma";

    when(traineeRepository.existsByUsername(anyString())).thenReturn(false);

    var e =
        assertThrows(
            EntityNotFoundException.class,
            () -> getTraineeTrainings.execute(username, null, null, null, null));

    assertNotNull(e);
    assertEquals(
        e.getMessage(), "Unable to find <Trainee> profile with username='%s'".formatted(username));

    verify(traineeRepository).existsByUsername(username);
    verify(trainingRepository, never()).findAll(ArgumentMatchers.<Specification<Training>>any());
    verify(mapper, never()).toTraineeTrainingResponse(any(Training.class));
  }

  @Test
  @DisplayName("<execute> should get trainee trainings and map them to DTO")
  void test_execute_shouldGetTrainingsAndMapToDto() {
    String username = "Gregor.Busk";
    LocalDate date = LocalDate.now();
    Training training = new Training();
    TraineeTrainingResponse resp =
        new TraineeTrainingResponse("Tr-1", date, null, "type", "John Johnson");

    when(traineeRepository.existsByUsername(anyString())).thenReturn(true);
    when(trainingRepository.findAll(ArgumentMatchers.<Specification<Training>>any()))
        .thenReturn(List.of(training));
    when(mapper.toTraineeTrainingResponse(any(Training.class))).thenReturn(resp);

    assertDoesNotThrow(
        () ->
            getTraineeTrainings.execute(
                username, date, date, resp.trainerName(), resp.trainingType()));

    verify(traineeRepository).existsByUsername(username);
    verify(trainingRepository).findAll(ArgumentMatchers.<Specification<Training>>any());
    verify(mapper).toTraineeTrainingResponse(training);
  }
}
