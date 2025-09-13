package com.epam.learn.gymservice.trainee.adapter.spi.persistence;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.trainee.domain.repository.TraineeRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TraineePersistenceAdapter implements TraineeRepository {

  private final JpaTraineeRepository repository;

  @Override
  public void deleteByUsername(String username) {
    repository.deleteByUser_Username(username);
  }

  @Override
  public boolean existsByUsername(String username) {
    return repository.existsByUser_Username(username);
  }

  @Override
  public Optional<Trainee> findByUsername(String username) {
    return repository.findByUser_Username(username);
  }

  @Override
  public Trainee getByUsername(String username) {
    return repository
        .findByUser_Username(username)
        .orElseThrow(() -> new EntityNotFoundException(Trainee.class, username));
  }

  @Override
  public Trainee getReferenceByUsername(String username) {
    Long id =
        repository
            .findIdByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException(Trainee.class, username));
    return repository.getReferenceById(id);
  }

  @Override
  public Trainee save(Trainee trainee) {
    return repository.persist(trainee);
  }

  @Override
  public void update(Trainee trainee) {
    repository.update(trainee);
  }
}
