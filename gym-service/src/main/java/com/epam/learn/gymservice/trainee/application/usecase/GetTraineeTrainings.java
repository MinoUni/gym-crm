package com.epam.learn.gymservice.trainee.application.usecase;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.trainee.adapter.api.rest.dto.TraineeTrainingResponse;
import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.trainee.domain.repository.TraineeRepository;
import com.epam.learn.gymservice.training.adapter.spi.persistence.TrainingMapper;
import com.epam.learn.gymservice.training.adapter.spi.persistence.TrainingSpecifications;
import com.epam.learn.gymservice.training.domain.repository.TrainingRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class GetTraineeTrainings {

  private final TrainingRepository trainingRepository;
  private final TraineeRepository traineeRepository;
  private final TrainingMapper mapper;

  public List<TraineeTrainingResponse> execute(
      String username, LocalDate from, LocalDate to, String trainerName, String trainingType) {
    if (!traineeRepository.existsByUsername(username)) {
      throw new EntityNotFoundException(Trainee.class, username);
    }
    return trainingRepository
        .findAll(
            Specification.allOf(
                Stream.of(
                        TrainingSpecifications.traineeUsernameEquals(username),
                        TrainingSpecifications.trainingDateBetween(from, to),
                        TrainingSpecifications.trainerNameLike(trainerName),
                        TrainingSpecifications.trainingTypeNameLike(trainingType))
                    .filter(Objects::nonNull)
                    .toList()))
        .stream()
        .map(mapper::toTraineeTrainingResponse)
        .toList();
  }
}
