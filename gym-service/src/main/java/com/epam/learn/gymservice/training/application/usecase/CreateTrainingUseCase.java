package com.epam.learn.gymservice.training.application.usecase;

import com.epam.learn.gymservice.infra.stereotype.DomainService;
import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.trainee.domain.repository.TraineeRepository;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.trainer.domain.repository.TrainerRepository;
import com.epam.learn.gymservice.training.adapter.api.rest.dto.TrainingCreateRequest;
import com.epam.learn.gymservice.training.adapter.spi.persistence.JpaTrainingTypeRepository;
import com.epam.learn.gymservice.training.adapter.spi.persistence.TrainingMapper;
import com.epam.learn.gymservice.training.domain.model.Training;
import com.epam.learn.gymservice.training.domain.model.TrainingType;
import com.epam.learn.gymservice.training.domain.repository.TrainingRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@Transactional
@AllArgsConstructor
public class CreateTrainingUseCase {

  private final TrainingMapper mapper;
  private final TraineeRepository traineeRepository;
  private final TrainerRepository trainerRepository;
  private final TrainingRepository trainingRepository;
  private final JpaTrainingTypeRepository trainingTypeRepository;

  public void execute(TrainingCreateRequest parameters) {
    Trainee traineeRef = traineeRepository.getReferenceByUsername(parameters.traineeUsername());
    Trainer trainerRef = trainerRepository.getReferenceByUsername(parameters.trainerUsername());
    TrainingType typRef = trainingTypeRepository.getReferenceById(parameters.trainingTypeId());
    Training trainingEntity = mapper.toEntity(parameters);
    trainingEntity.setTrainee(traineeRef);
    trainingEntity.setTrainer(trainerRef);
    trainingEntity.setType(typRef);
    trainingRepository.save(trainingEntity);
  }
}
