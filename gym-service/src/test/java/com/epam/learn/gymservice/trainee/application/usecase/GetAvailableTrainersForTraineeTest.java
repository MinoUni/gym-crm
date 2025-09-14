package com.epam.learn.gymservice.trainee.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.trainee.domain.repository.TraineeRepository;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.trainer.domain.repository.TrainerRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetAvailableTrainersForTraineeTest {

  public static final String USERNAME = "Abbigail";

  @Mock private TraineeRepository traineeRepository;
  @Mock private TrainerRepository trainerRepository;
  @InjectMocks private GetAvailableTrainersForTrainee getAvailableTrainersForTrainee;

  @Test
  @DisplayName("<execute> should throw <EntityNotFoundException>")
  void test_execute_shouldThrowTraineeNotFoundException() {
    when(traineeRepository.existsByUsername(anyString())).thenReturn(false);

    assertThrows(
        EntityNotFoundException.class, () -> getAvailableTrainersForTrainee.execute(USERNAME));

    verify(traineeRepository).existsByUsername(USERNAME);
    verify(trainerRepository, never()).findActiveTrainersNotAssignedToTrainee(USERNAME);
  }

  @Test
  @DisplayName("<execute> should return list of available trainers")
  void test_execute_shouldReturnAvailableTrainersList() {
    List<Trainer> trainers = List.of(new Trainer());

    when(traineeRepository.existsByUsername(anyString())).thenReturn(true);
    when(trainerRepository.findActiveTrainersNotAssignedToTrainee(anyString()))
        .thenReturn(trainers);

    var result = assertDoesNotThrow(() -> getAvailableTrainersForTrainee.execute(USERNAME));

    assertNotNull(result);
    assertEquals(trainers, result);

    verify(traineeRepository).existsByUsername(USERNAME);
    verify(trainerRepository).findActiveTrainersNotAssignedToTrainee(USERNAME);
  }
}
