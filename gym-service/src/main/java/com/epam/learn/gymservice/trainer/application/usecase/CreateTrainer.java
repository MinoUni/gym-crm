package com.epam.learn.gymservice.trainer.application.usecase;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.infra.stereotype.DomainService;
import com.epam.learn.gymservice.trainer.adapter.api.rest.dto.TrainerCreateRequest;
import com.epam.learn.gymservice.trainer.adapter.spi.persistence.TrainerMapper;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.trainer.domain.repository.TrainerRepository;
import com.epam.learn.gymservice.training.domain.model.TrainingType;
import com.epam.learn.gymservice.training.domain.repository.TrainingTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@Transactional
@AllArgsConstructor
public class CreateTrainer {

  private final TrainerMapper mapper;
  private final TrainerRepository trainerRepository;
  private final TrainingTypeRepository trainingTypeRepository;

  public Trainer execute(TrainerCreateRequest parameters) {
    if (!trainingTypeRepository.existsById(parameters.trainingTypeId())) {
      throw new EntityNotFoundException(TrainingType.class, parameters.trainingTypeId());
    }
    Trainer trainer = mapper.toEntity(parameters);
    trainer.setSpecialization(trainingTypeRepository.getReferenceById(parameters.trainingTypeId()));
    return trainerRepository.save(trainer);
  }
}
