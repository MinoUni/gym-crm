package com.epam.learn.gymservice.trainer.application.usecase;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.trainer.adapter.api.rest.dto.TrainerUpdateRequest;
import com.epam.learn.gymservice.trainer.adapter.spi.persistence.TrainerMapper;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.trainer.domain.repository.TrainerRepository;
import com.epam.learn.gymservice.training.domain.repository.TrainingTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UpdateTrainerProfile {

  private final TrainerMapper mapper;
  private final TrainerRepository trainerRepository;
  private final TrainingTypeRepository trainingTypeRepository;

  public Trainer execute(String username, TrainerUpdateRequest parameters) {
    if (!trainingTypeRepository.existsById(parameters.specializationRef())) {
      throw new EntityNotFoundException(
          TrainingTypeRepository.class, parameters.specializationRef());
    }
    Trainer trainer = trainerRepository.getByUsername(username);
    mapper.fullyUpdate(trainer, parameters);
    trainer.setSpecialization(
        trainingTypeRepository.getReferenceById(parameters.specializationRef()));
    return trainerRepository.save(trainer);
  }
}
