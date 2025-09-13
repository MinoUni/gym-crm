package com.epam.learn.gymservice.trainee.domain.repository;

import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import java.util.Optional;

public interface TraineeRepository {

  void deleteByUsername(String username);

  boolean existsByUsername(String username);

  Optional<Trainee> findByUsername(String username);

  Trainee getByUsername(String username);

  Trainee getReferenceByUsername(String username);

  Trainee save(Trainee trainee);

  void update(Trainee trainee);
}
