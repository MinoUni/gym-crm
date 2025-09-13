package com.epam.learn.gymservice.trainer.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.trainer.adapter.api.rest.dto.TrainerUpdateRequest;
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
class UpdateTrainerProfileTest {

  public static final String USERNAME = "Ada.Wong";

  @Mock private TrainerMapper mapper;
  @Mock private TrainerRepository trainerRepository;
  @Mock private TrainingTypeRepository trainingTypeRepository;

  @InjectMocks private UpdateTrainerProfile updateTrainerProfile;

  @Test
  @DisplayName(
      "<execute> should throw <EntityNotFoundException> when provided invalid 'specializationRef'")
  void test_execute_shouldThrowEntityNotFoundException() {
    TrainerUpdateRequest params = new TrainerUpdateRequest("Ada", "Wong", true, 1L);

    when(trainingTypeRepository.existsById(anyLong())).thenReturn(false);

    assertThrows(
        EntityNotFoundException.class, () -> updateTrainerProfile.execute(USERNAME, params));

    verify(trainingTypeRepository).existsById(params.specializationRef());
    verify(trainerRepository, never()).getByUsername(USERNAME);
    verify(mapper, never()).fullyUpdate(any(Trainer.class), eq(params));
    verify(trainingTypeRepository, never()).getReferenceById(params.specializationRef());
    verify(trainerRepository, never()).save(any(Trainer.class));
  }

  @Test
  @DisplayName("<execute> should update and return trainer profile details")
  void test_execute_shouldUpdateTrainer() {
    TrainerUpdateRequest params = new TrainerUpdateRequest("Ada", "Wong", true, 1L);
    Trainer trainer = new Trainer();
    TrainingType specialization = new TrainingType();

    when(trainingTypeRepository.existsById(anyLong())).thenReturn(true);
    when(trainerRepository.getByUsername(anyString())).thenReturn(trainer);
    doNothing().when(mapper).fullyUpdate(any(Trainer.class), any(TrainerUpdateRequest.class));
    when(trainingTypeRepository.getReferenceById(anyLong())).thenReturn(specialization);
    when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

    assertDoesNotThrow(() -> updateTrainerProfile.execute(USERNAME, params));
    assertNotNull(trainer.getSpecialization());
    assertEquals(specialization, trainer.getSpecialization());

    verify(trainingTypeRepository).existsById(params.specializationRef());
    verify(trainerRepository).getByUsername(USERNAME);
    verify(mapper).fullyUpdate(any(Trainer.class), eq(params));
    verify(trainingTypeRepository).getReferenceById(params.specializationRef());
    verify(trainerRepository).save(any(Trainer.class));
  }
}
