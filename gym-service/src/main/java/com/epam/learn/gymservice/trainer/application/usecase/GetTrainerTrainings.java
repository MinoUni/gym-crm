package com.epam.learn.gymservice.trainer.application.usecase;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.infra.stereotype.DomainService;
import com.epam.learn.gymservice.trainer.adapter.api.rest.dto.TrainerTrainingResponse;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.trainer.domain.repository.TrainerRepository;
import com.epam.learn.gymservice.training.adapter.spi.persistence.TrainingMapper;
import com.epam.learn.gymservice.training.adapter.spi.persistence.TrainingSpecifications;
import com.epam.learn.gymservice.training.domain.repository.TrainingRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@AllArgsConstructor
@Transactional(readOnly = true)
public class GetTrainerTrainings {

  private final TrainingMapper mapper;
  private final TrainerRepository trainerRepository;
  private final TrainingRepository trainingRepository;

  public List<TrainerTrainingResponse> execute(
      String trainerUsername, LocalDate from, LocalDate to, String traineeName) {
    if (!trainerRepository.existsByUsername(trainerUsername)) {
      throw new EntityNotFoundException(Trainer.class, trainerUsername);
    }
    return trainingRepository
        .findAll(
            Specification.allOf(
                Stream.of(
                        TrainingSpecifications.trainerUsernameEquals(trainerUsername),
                        TrainingSpecifications.trainingDateBetween(from, to),
                        TrainingSpecifications.traineeNameLike(traineeName))
                    .filter(Objects::nonNull)
                    .toList()))
        .stream()
        .map(mapper::toTrainerTrainingResponse)
        .toList();
  }
}
