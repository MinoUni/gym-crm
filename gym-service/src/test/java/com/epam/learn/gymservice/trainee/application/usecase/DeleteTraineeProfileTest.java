package com.epam.learn.gymservice.trainee.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import com.epam.learn.gymservice.trainee.domain.repository.TraineeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteTraineeProfileTest {

  public static final String USERNAME = "John.Doe";

  @Mock private TraineeRepository repository;
  @InjectMocks private DeleteTraineeProfile useCase;

  @Test
  @DisplayName("'execute' should delete trainee by username")
  void test_execute_shouldDeleteEntity() {
    doNothing().when(repository).deleteByUsername(anyString());

    assertDoesNotThrow(() -> useCase.execute(USERNAME));

    verify(repository).deleteByUsername(USERNAME);
  }
}
