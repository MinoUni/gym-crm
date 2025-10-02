package com.epam.learn.trsessionservice.domain.repository;

import com.epam.learn.trsessionservice.domain.model.TrainingSession;
import java.util.Optional;

public interface TrainingSessionRepository {

  TrainingSession getByTrainerUsername(String trainerUsername);

  Optional<TrainingSession> findById(Long id);

  TrainingSession getById(Long id);

  TrainingSession save(TrainingSession trainingSession);
}
