package com.epam.learn.gymservice.training.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.trainee.domain.repository.TraineeRepository;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.trainer.domain.repository.TrainerRepository;
import com.epam.learn.gymservice.training.adapter.api.rest.dto.TrainingCreateRequest;
import com.epam.learn.gymservice.training.adapter.spi.persistence.TrainingMapper;
import com.epam.learn.gymservice.training.domain.model.Training;
import com.epam.learn.gymservice.training.domain.model.TrainingType;
import com.epam.learn.gymservice.training.domain.repository.TrainingRepository;
import com.epam.learn.gymservice.training.domain.repository.TrainingTypeRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateTrainingUseCaseTest {

  @Mock private TrainingMapper mapper;
  @Mock private TraineeRepository traineeRepository;
  @Mock private TrainerRepository trainerRepository;
  @Mock private TrainingRepository trainingRepository;
  @Mock private TrainingTypeRepository trainingTypeRepository;

  @InjectMocks private CreateTrainingUseCase createTraining;

  @Test
  @DisplayName("<execute> should save new training")
  void test_execute_shouldSaveNewTraining() {
    TrainingCreateRequest req =
        new TrainingCreateRequest("TR", LocalDate.now(), LocalTime.now(), 1L, "trainer", "trainee");
    Trainee trainee = new Trainee();
    Trainer trainer = new Trainer();
    TrainingType type = new TrainingType();
    Training training = new Training();

    when(traineeRepository.getReferenceByUsername(anyString())).thenReturn(trainee);
    when(trainerRepository.getReferenceByUsername(anyString())).thenReturn(trainer);
    when(trainingTypeRepository.getReferenceById(anyLong())).thenReturn(type);
    when(mapper.toEntity(any(TrainingCreateRequest.class))).thenReturn(training);
    when(trainingRepository.save(any(Training.class))).thenReturn(training);

    assertDoesNotThrow(() -> createTraining.execute(req));
    assertNotNull(training.getType());
    assertNotNull(training.getTrainer());
    assertNotNull(training.getTrainee());

    verify(traineeRepository).getReferenceByUsername(req.traineeUsername());
    verify(trainerRepository).getReferenceByUsername(req.trainerUsername());
    verify(trainingTypeRepository).getReferenceById(req.trainingTypeId());
    verify(mapper).toEntity(req);
    verify(trainingRepository).save(training);
  }
}
