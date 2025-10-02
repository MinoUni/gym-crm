package com.epam.learn.trsessionservice.adapter.spi.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoTrainingSessionRepository
    extends MongoRepository<TrainingSessionEntity, Long> {

  TrainingSessionEntity getByTrainerUsername(String trainerUsername);
}
