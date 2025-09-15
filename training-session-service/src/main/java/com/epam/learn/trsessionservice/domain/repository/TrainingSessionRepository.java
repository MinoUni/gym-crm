package com.epam.learn.trsessionservice.domain.repository;

import com.epam.learn.trsessionservice.domain.model.TrainingSession;
import java.util.List;

public interface TrainingSessionRepository {

  List<TrainingSession> findAllByTrainerUsername(String trainerUsername);

  TrainingSession getById(Long id);

  TrainingSession save(TrainingSession trainingSession);
}
