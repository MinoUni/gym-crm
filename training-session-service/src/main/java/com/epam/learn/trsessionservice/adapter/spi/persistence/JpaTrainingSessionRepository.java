package com.epam.learn.trsessionservice.adapter.spi.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTrainingSessionRepository extends JpaRepository<TrainingSessionEntity, Long> {

  List<TrainingSessionEntity> findAllByTrainerUsername(String trainerUsername);
}
