package com.epam.learn.gymservice.trainee.application.usecase;

import com.epam.learn.gymservice.trainee.adapter.api.rest.dto.TraineeUpdateRequest;
import com.epam.learn.gymservice.trainee.adapter.spi.persistence.TraineeMapper;
import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.trainee.domain.repository.TraineeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UpdateTraineeProfile {

  private final TraineeRepository repository;
  private final TraineeMapper mapper;

  public Trainee execute(String username, TraineeUpdateRequest parameters) {
    Trainee trainee = repository.getByUsername(username);
    mapper.fullyUpdate(trainee, parameters);
    repository.update(trainee);
    return trainee;
  }
}
