package com.epam.learn.trsessionservice.application.usecase;

import com.epam.learn.trsessionservice.adapter.api.rest.dto.TrainingSessionEvent;
import com.epam.learn.trsessionservice.adapter.spi.persistence.TrainingSessionMapper;
import com.epam.learn.trsessionservice.domain.model.ActionType;
import com.epam.learn.trsessionservice.domain.repository.TrainingSessionRepository;
import com.epam.learn.trsessionservice.domain.service.TrainingSessionHandler;
import lombok.AllArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class HandleTrainingSessionUseCase implements TrainingSessionHandler {

  private final TrainingSessionMapper mapper;
  private final TrainingSessionRepository repository;

  @Override
  @JmsListener(destination = "training.session.queue", containerFactory = "myFactory")
  public void handle(TrainingSessionEvent event) {
    if (event.actionType() == ActionType.ADD) {
      repository
          .findById(event.id())
          .ifPresentOrElse(
              session -> {
                session.addYear(mapper.buildYear(event.trainingDate(), event.trainingDuration()));
                repository.save(session);
              },
              () -> repository.save(mapper.toDomain(event)));
    }
  }
}
