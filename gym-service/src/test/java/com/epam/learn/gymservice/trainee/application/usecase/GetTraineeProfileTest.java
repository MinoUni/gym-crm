package com.epam.learn.gymservice.trainee.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.trainee.domain.repository.TraineeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetTraineeProfileTest {

  private static final String USERNAME = "John.Doe";

  @Mock private TraineeRepository repository;

  @InjectMocks private GetTraineeProfile useCase;

  @Test
  @DisplayName("<execute> should return trainee entity")
  void test_execute_shouldReturnEntity() {
    Trainee trainee = new Trainee();

    when(repository.getByUsername(anyString())).thenReturn(trainee);

    Trainee result = assertDoesNotThrow(() -> useCase.execute(USERNAME));

    assertNotNull(result);
    assertEquals(trainee, result);

    verify(repository).getByUsername(USERNAME);
  }

  @Test
  @DisplayName("<execute> should throw <EntityNotFoundException>")
  void test_execute_shouldThrowEntityNotFoundExceptionException() {

    when(repository.getByUsername(anyString())).thenThrow(EntityNotFoundException.class);

    assertThrows(EntityNotFoundException.class, () -> useCase.execute(USERNAME));

    verify(repository).getByUsername(USERNAME);
  }
}
