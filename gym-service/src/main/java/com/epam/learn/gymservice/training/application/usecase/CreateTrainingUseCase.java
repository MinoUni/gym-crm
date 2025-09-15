package com.epam.learn.gymservice.training.application.usecase;

import com.epam.learn.gymservice.infra.stereotype.DomainService;
import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.trainee.domain.repository.TraineeRepository;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.trainer.domain.repository.TrainerRepository;
import com.epam.learn.gymservice.training.adapter.api.rest.dto.TrainingCreateRequest;
import com.epam.learn.gymservice.training.adapter.api.rest.dto.TrainingSessionEvent;
import com.epam.learn.gymservice.training.adapter.spi.persistence.TrainingMapper;
import com.epam.learn.gymservice.training.domain.model.Training;
import com.epam.learn.gymservice.training.domain.model.TrainingType;
import com.epam.learn.gymservice.training.domain.repository.TrainingRepository;
import com.epam.learn.gymservice.training.domain.repository.TrainingTypeRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.LocalTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Slf4j
@DomainService
@Transactional
public class CreateTrainingUseCase {

  private final RestClient restClient;
  private final TrainingMapper mapper;
  private final TraineeRepository traineeRepository;
  private final TrainerRepository trainerRepository;
  private final TrainingRepository trainingRepository;
  private final TrainingTypeRepository trainingTypeRepository;

  public CreateTrainingUseCase(
      TrainingMapper mapper,
      TraineeRepository traineeRepository,
      TrainerRepository trainerRepository,
      TrainingRepository trainingRepository,
      TrainingTypeRepository trainingTypeRepository,
      RestClient.Builder restClientBuilder) {
    this.mapper = mapper;
    this.traineeRepository = traineeRepository;
    this.trainerRepository = trainerRepository;
    this.trainingRepository = trainingRepository;
    this.trainingTypeRepository = trainingTypeRepository;
    this.restClient = restClientBuilder.baseUrl("http://localhost:8082").build();
  }

  @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallbackMethod")
  public void execute(TrainingCreateRequest parameters) {
    Trainee traineeRef = traineeRepository.getReferenceByUsername(parameters.traineeUsername());
    Trainer trainer = trainerRepository.getByUsername(parameters.trainerUsername());
    TrainingType typRef = trainingTypeRepository.getReferenceById(parameters.trainingTypeId());
    Training trainingEntity = mapper.toEntity(parameters);
    trainingEntity.setTrainee(traineeRef);
    trainingEntity.setTrainer(trainer);
    trainingEntity.setType(typRef);
    trainingEntity = trainingRepository.save(trainingEntity);

    String jwt =
        ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
            .getToken()
            .getTokenValue();

    restClient
        .post()
        .uri("/api/v1/training-sessions")
        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwt))
        .accept(MediaType.APPLICATION_JSON)
        .body(buildEvent(trainingEntity))
        .retrieve()
        .toBodilessEntity();
  }

  private void fallbackMethod(Throwable e) {
    log.error(
        "Something went wrong with <training-sessions-service>. Caused by: {}", e.getMessage());
  }

  private TrainingSessionEvent buildEvent(Training training) {
    final int hours = 24;
    final int minutes = 60;
    final int seconds = 60;
    return new TrainingSessionEvent(
        training.getId(),
        training.getTrainer().getUser().getUsername(),
        training.getTrainer().getUser().getFirstName(),
        training.getTrainer().getUser().getLastName(),
        training.getTrainer().getUser().isActive(),
        training.getTrainingDate(),
        LocalTime.ofSecondOfDay(training.getDuration().getSeconds() % (hours * minutes * seconds)),
        TrainingSessionEvent.ActionType.ADD);
  }
}
