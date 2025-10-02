package com.epam.learn.trsessionservice.adapter.spi.persistence;

import com.epam.learn.trsessionservice.domain.model.TrainingSession;
import com.epam.learn.trsessionservice.domain.repository.TrainingSessionRepository;
import com.epam.learn.trsessionservice.infra.exception.TrainingSessionNotFoundException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TrainingSessionPersistenceAdapter implements TrainingSessionRepository {

  private final MongoTrainingSessionRepository repository;
  private final TrainingSessionMapper mapper;

  @Override
  public TrainingSession getByTrainerUsername(String trainerUsername) {
    return mapper.toDomain(repository.getByTrainerUsername(trainerUsername));
  }

  @Override
  public Optional<TrainingSession> findById(Long id) {
    return repository.findById(id).map(mapper::toDomain);
  }

  @Override
  public TrainingSession getById(Long id) {
    return mapper.toDomain(
        repository.findById(id).orElseThrow(() -> new TrainingSessionNotFoundException(id)));
  }

  @Override
  public TrainingSession save(TrainingSession trainingSession) {
    return mapper.toDomain(repository.save(mapper.toEntity(trainingSession)));
  }
}
