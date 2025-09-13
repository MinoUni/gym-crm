package com.epam.learn.gymservice.trainer.domain.repository;

import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import java.util.Collection;
import java.util.List;

public interface TrainerRepository {

  Trainer save(Trainer trainer);

  long countByUsername(Collection<String> usernames);

  boolean existsByUsername(String username);

  List<Trainer> findActiveTrainersNotAssignedToTrainee(String traineeUsername);

  List<Trainer> findAllByUsernameIn(Collection<String> usernames);

  Trainer getByUsername(String username);

  Trainer getReferenceByUsername(String username);
}
