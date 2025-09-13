package com.epam.learn.gymservice.training.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.learn.gymservice.training.adapter.api.rest.dto.TrainingTypeResponse;
import com.epam.learn.gymservice.training.adapter.spi.persistence.TrainingMapper;
import com.epam.learn.gymservice.training.domain.model.TrainingType;
import com.epam.learn.gymservice.training.domain.repository.TrainingTypeRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetTrainingTypesUseCaseTest {

  @Mock private TrainingTypeRepository repository;
  @Mock private TrainingMapper mapper;
  @InjectMocks private GetTrainingTypesUseCase getTrainingTypes;

  @Test
  @DisplayName("<execute> should return empty list")
  void test_execute_shouldReturnEmptyList() {
    when(repository.findAll()).thenReturn(List.of());

    var result = assertDoesNotThrow(getTrainingTypes::execute);
    assertNotNull(result);
    assertTrue(result.isEmpty());

    verify(repository).findAll();
    verify(mapper, never()).toTrainingTypeResponse(any(TrainingType.class));
  }

  @Test
  @DisplayName("<execute> should return list of training types")
  void test_execute_shouldReturnTrainingTypeList() {
    List<TrainingType> trainingTypeList = List.of(new TrainingType(), new TrainingType());
    TrainingTypeResponse resp = new TrainingTypeResponse(1L, "TR");

    when(repository.findAll()).thenReturn(trainingTypeList);
    when(mapper.toTrainingTypeResponse(any(TrainingType.class))).thenReturn(resp);

    var result = assertDoesNotThrow(getTrainingTypes::execute);
    assertNotNull(result);
    assertEquals(trainingTypeList.size(), result.size());

    verify(repository).findAll();
    verify(mapper, times(trainingTypeList.size())).toTrainingTypeResponse(any(TrainingType.class));
  }
}
