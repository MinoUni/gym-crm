package com.epam.learn.gymservice.training.application.usecase;

import com.epam.learn.gymservice.infra.stereotype.DomainService;
import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.trainee.domain.repository.TraineeRepository;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.trainer.domain.repository.TrainerRepository;
import com.epam.learn.gymservice.training.adapter.api.rest.dto.TrainingCreateRequest;
import com.epam.learn.gymservice.training.adapter.api.rest.dto.TrainingSessionEvent;
import com.epam.learn.gymservice.training.adapter.spi.messaging.TrainingEventPublisher;
import com.epam.learn.gymservice.training.adapter.spi.persistence.TrainingMapper;
import com.epam.learn.gymservice.training.domain.model.Training;
import com.epam.learn.gymservice.training.domain.model.TrainingType;
import com.epam.learn.gymservice.training.domain.repository.TrainingRepository;
import com.epam.learn.gymservice.training.domain.repository.TrainingTypeRepository;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@DomainService
@Transactional
@AllArgsConstructor
public class CreateTrainingUseCase {

  private final TrainingMapper mapper;
  private final TrainingEventPublisher publisher;
  private final TraineeRepository traineeRepository;
  private final TrainerRepository trainerRepository;
  private final TrainingRepository trainingRepository;
  private final TrainingTypeRepository trainingTypeRepository;

  public void execute(TrainingCreateRequest parameters) {
    Trainee traineeRef = traineeRepository.getReferenceByUsername(parameters.traineeUsername());
    Trainer trainer = trainerRepository.getByUsername(parameters.trainerUsername());
    TrainingType typRef = trainingTypeRepository.getReferenceById(parameters.trainingTypeId());

    Training trainingEntity = mapper.toEntity(parameters);

    trainingEntity.setTrainee(traineeRef);
    trainingEntity.setTrainer(trainer);
    trainingEntity.setType(typRef);

    trainingEntity = trainingRepository.save(trainingEntity);

    publisher.publishEvent(buildEvent(trainingEntity));
  }

  private TrainingSessionEvent buildEvent(Training training) {
    final int hours = 24;
    final int minutes = 60;
    final int seconds = 60;
    return new TrainingSessionEvent(
        training.getId(),
        training.getTrainer().getUser().getUsername(),
        training.getTrainer().getUser().getFirstName(),
        training.getTrainer().getUser().getLastName(),
        training.getTrainer().getUser().isActive(),
        training.getTrainingDate(),
        LocalTime.ofSecondOfDay(training.getDuration().getSeconds() % (hours * minutes * seconds)),
        TrainingSessionEvent.ActionType.ADD);
  }
}
