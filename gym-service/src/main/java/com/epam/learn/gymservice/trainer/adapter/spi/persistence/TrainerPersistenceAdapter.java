package com.epam.learn.gymservice.trainer.adapter.spi.persistence;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.trainer.domain.repository.TrainerRepository;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TrainerPersistenceAdapter implements TrainerRepository {

  private final JpaTrainerRepository repository;

  @Override
  public Trainer save(Trainer trainer) {
    return repository.persist(trainer);
  }

  @Override
  public long countByUsername(Collection<String> usernames) {
    return repository.countAllByUser_Username(usernames);
  }

  @Override
  public boolean existsByUsername(String username) {
    return repository.existsByUser_Username(username);
  }

  @Override
  public List<Trainer> findActiveTrainersNotAssignedToTrainee(String traineeUsername) {
    return repository.findActiveTrainersNotAssignedToTrainee(traineeUsername);
  }

  @Override
  public List<Trainer> findAllByUsernameIn(Collection<String> usernames) {
    return repository.findAllByUser_UsernameIn(usernames);
  }

  @Override
  public Trainer getByUsername(String username) {
    return repository
        .findByUser_Username(username)
        .orElseThrow(() -> new EntityNotFoundException(Trainer.class, username));
  }

  @Override
  public Trainer getReferenceByUsername(String username) {
    Long id =
        repository
            .findIdByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException(Trainer.class, username));
    return repository.getReferenceById(id);
  }
}
