package com.epam.learn.gymservice.trainer.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.trainer.adapter.api.rest.dto.TrainerCreateRequest;
import com.epam.learn.gymservice.trainer.adapter.spi.persistence.TrainerMapper;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.trainer.domain.repository.TrainerRepository;
import com.epam.learn.gymservice.training.domain.model.TrainingType;
import com.epam.learn.gymservice.training.domain.repository.TrainingTypeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateTrainerTest {

  @Mock private TrainerMapper mapper;
  @Mock private TrainerRepository trainerRepository;
  @Mock private TrainingTypeRepository trainingTypeRepository;

  @InjectMocks private CreateTrainer createTrainer;

  @Test
  @DisplayName(
      "<execute> should throw <EntityNotFoundException> when training type reference is invalid")
  void test_execute_shouldThrowEntityNotFoundException_whenTrainingTypeRefIsInvalid() {
    TrainerCreateRequest request = new TrainerCreateRequest("f_n", "l_n", -1L);

    when(trainingTypeRepository.existsById(anyLong())).thenReturn(false);

    assertThrows(EntityNotFoundException.class, () -> createTrainer.execute(request));

    verify(trainingTypeRepository).existsById(request.trainingTypeId());
    verify(mapper, never()).toEntity(request);
    verify(trainingTypeRepository, never()).getReferenceById(request.trainingTypeId());
    verify(trainerRepository, never()).save(any(Trainer.class));
  }

  @Test
  @DisplayName("<execute> should save new trainer, then return it")
  void test_execute_shouldSaveNewTrainer_thenReturnIt() {
    TrainerCreateRequest request = new TrainerCreateRequest("f_n", "l_n", 1L);
    Trainer trainer = new Trainer();
    TrainingType specializationRef = new TrainingType();

    when(trainingTypeRepository.existsById(anyLong())).thenReturn(true);
    when(mapper.toEntity(any(TrainerCreateRequest.class))).thenReturn(trainer);
    when(trainingTypeRepository.getReferenceById(anyLong())).thenReturn(specializationRef);
    when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

    var result = assertDoesNotThrow(() -> createTrainer.execute(request));
    assertNotNull(result);
    assertEquals(specializationRef, trainer.getSpecialization());

    verify(trainingTypeRepository).existsById(request.trainingTypeId());
    verify(mapper).toEntity(request);
    verify(trainingTypeRepository).getReferenceById(request.trainingTypeId());
    verify(trainerRepository).save(trainer);
  }
}
