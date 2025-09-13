package com.epam.learn.gymservice.trainee.application.usecase;

import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.trainee.domain.repository.TraineeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class GetTraineeProfile {

  private final TraineeRepository repository;

  public Trainee execute(String username) {
    return repository.getByUsername(username);
  }
}
