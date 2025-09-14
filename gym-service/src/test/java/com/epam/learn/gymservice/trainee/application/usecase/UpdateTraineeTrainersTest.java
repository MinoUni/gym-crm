package com.epam.learn.gymservice.trainee.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.trainee.domain.model.Trainee;
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
class UpdateTraineeTrainersTest {

  @Mock private TraineeRepository traineeRepository;
  @Mock private TrainerRepository trainerRepository;

  @InjectMocks private UpdateTraineeTrainers updateTraineeTrainers;

  @Test
  @DisplayName("<execute> should throw <EntityNotFoundException> when some trainers not found")
  void test_execute_shouldThrowEntityNotFoundException_whenTrainerNotFound() {
    String traineeUsername = "Bob";
    List<String> trainerUsernames = List.of("Jack", "S.A.M");
    String expectedMsg =
        "Unable to find <%s> profiles with usernames=[%s]"
            .formatted(Trainer.class.getSimpleName(), String.join(",", trainerUsernames));

    when(trainerRepository.countByUsername(anyCollection())).thenReturn(0L);

    var e =
        assertThrows(
            EntityNotFoundException.class,
            () -> updateTraineeTrainers.execute(traineeUsername, trainerUsernames));

    assertNotNull(e);
    assertEquals(expectedMsg, e.getMessage());

    verify(trainerRepository).countByUsername(trainerUsernames);
    verify(traineeRepository, never()).getByUsername(traineeUsername);
    verify(trainerRepository, never()).findAllByUsernameIn(trainerUsernames);
    verify(traineeRepository, never()).update(any(Trainee.class));
  }

  @Test
  @DisplayName("<execute> should throw <EntityNotFoundException> when trainee not found")
  void test_executeShouldThrowTraineeNotFoundException_whenTraineeNotFound() {
    String traineeUsername = "Bob";
    List<String> trainerUsernames = List.of("Jack", "S.A.M");

    when(trainerRepository.countByUsername(anyCollection()))
        .thenReturn(Long.valueOf(trainerUsernames.size()));
    when(traineeRepository.getByUsername(anyString())).thenThrow(EntityNotFoundException.class);

    assertThrows(
        EntityNotFoundException.class,
        () -> updateTraineeTrainers.execute(traineeUsername, trainerUsernames));

    verify(trainerRepository).countByUsername(trainerUsernames);
    verify(traineeRepository).getByUsername(traineeUsername);
    verify(trainerRepository, never()).findAllByUsernameIn(trainerUsernames);
    verify(traineeRepository, never()).update(any(Trainee.class));
  }

  @Test
  @DisplayName("<execute> should update trainee trainers list")
  void test_execute_shouldUpdateTraineeTrainersList() {
    String traineeUsername = "Bob";
    List<String> trainerUsernames = List.of("Jack", "S.A.M");
    Trainee trainee = new Trainee();

    when(trainerRepository.countByUsername(anyCollection()))
        .thenReturn(Long.valueOf(trainerUsernames.size()));
    when(traineeRepository.getByUsername(anyString())).thenReturn(trainee);
    when(trainerRepository.findAllByUsernameIn(anyList()))
        .thenReturn(List.of(new Trainer(), new Trainer()));
    doNothing().when(traineeRepository).update(any(Trainee.class));

    assertDoesNotThrow(() -> updateTraineeTrainers.execute(traineeUsername, trainerUsernames));

    assertEquals(2, trainee.getTrainers().size());

    verify(trainerRepository).countByUsername(trainerUsernames);
    verify(traineeRepository).getByUsername(traineeUsername);
    verify(trainerRepository).findAllByUsernameIn(trainerUsernames);
    verify(traineeRepository).update(any(Trainee.class));
  }
}
