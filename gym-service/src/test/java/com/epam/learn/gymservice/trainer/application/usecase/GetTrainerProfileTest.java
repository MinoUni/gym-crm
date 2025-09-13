package com.epam.learn.gymservice.trainer.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.trainer.domain.repository.TrainerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetTrainerProfileTest {

  public static final String USERNAME = "Ada.Wong";

  @Mock private TrainerRepository repository;

  @InjectMocks private GetTrainerProfile getTrainerProfile;

  @Test
  @DisplayName("<execute> should throw <EntityNotFoundException> when invalid username provided")
  void test_execute_shouldThrowEntityNotFoundException() {
    when(repository.getByUsername(anyString())).thenThrow(EntityNotFoundException.class);

    assertThrows(EntityNotFoundException.class, () -> getTrainerProfile.execute(USERNAME));

    verify(repository).getByUsername(USERNAME);
  }

  @Test
  @DisplayName("<execute> should fetch and return trainer profile details")
  void test_execute_shouldReturnTrainerProfile() {
    when(repository.getByUsername(anyString())).thenReturn(new Trainer());

    assertDoesNotThrow(() -> getTrainerProfile.execute(USERNAME));

    verify(repository).getByUsername(USERNAME);
  }
}
