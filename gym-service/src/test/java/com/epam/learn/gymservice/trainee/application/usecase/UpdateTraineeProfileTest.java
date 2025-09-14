package com.epam.learn.gymservice.trainee.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.trainee.adapter.api.rest.dto.TraineeUpdateRequest;
import com.epam.learn.gymservice.trainee.adapter.spi.persistence.TraineeMapper;
import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.trainee.domain.repository.TraineeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateTraineeProfileTest {

  public static final String USERNAME = "John.Doe";

  @Mock private TraineeRepository repository;
  @Mock private TraineeMapper mapper;

  @InjectMocks private UpdateTraineeProfile useCase;

  @Test
  @DisplayName("<execute> should update and return updated entity")
  void test_execute_shouldUpdateAndReturnUpdatedEntity() {
    TraineeUpdateRequest request = new TraineeUpdateRequest("Karl", "Shimmer", null, null, true);
    Trainee trainee = new Trainee();

    when(repository.getByUsername(anyString())).thenReturn(trainee);
    doNothing().when(mapper).fullyUpdate(any(Trainee.class), any(TraineeUpdateRequest.class));
    doNothing().when(repository).update(any(Trainee.class));

    Trainee result = assertDoesNotThrow(() -> useCase.execute(USERNAME, request));

    assertNotNull(result);
    assertEquals(trainee, result);

    verify(repository).getByUsername(USERNAME);
    verify(mapper).fullyUpdate(trainee, request);
    verify(repository).update(trainee);
  }

  @Test
  @DisplayName("<execute> should throw <EntityNotFoundException> exception")
  void test_execute_shouldThrowException() {
    TraineeUpdateRequest request = new TraineeUpdateRequest("Karl", "Shimmer", null, null, true);

    when(repository.getByUsername(anyString())).thenThrow(EntityNotFoundException.class);

    assertThrows(EntityNotFoundException.class, () -> useCase.execute(USERNAME, request));

    verify(repository).getByUsername(USERNAME);
    verify(mapper, never()).fullyUpdate(any(Trainee.class), eq(request));
    verify(repository, never()).update(any(Trainee.class));
  }
}
