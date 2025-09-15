package com.epam.learn.trsessionservice.application.usecase;

import com.epam.learn.trsessionservice.adapter.api.rest.dto.TrainingSessionEvent;
import com.epam.learn.trsessionservice.adapter.spi.persistence.TrainingSessionMapper;
import com.epam.learn.trsessionservice.domain.model.ActionType;
import com.epam.learn.trsessionservice.domain.model.TrainingSession;
import com.epam.learn.trsessionservice.domain.repository.TrainingSessionRepository;
import com.epam.learn.trsessionservice.domain.service.TrainingSessionHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HandleTrainingSessionUseCase implements TrainingSessionHandler {

  private final TrainingSessionRepository repository;
  private final TrainingSessionMapper mapper;

  @Override
  public void handle(TrainingSessionEvent event) {
    if (event.actionType() == ActionType.ADD) {
      TrainingSession trainingSession = mapper.toDomain(event);
      repository.save(trainingSession);
      return;
    }
    if (event.actionType() == ActionType.DELETE) {
      TrainingSession trainingSession = repository.getById(event.id());
      trainingSession.setIsActive(false);
      repository.save(trainingSession);
    }
  }
}
