package com.epam.learn.gymservice.trainee.application.usecase;

import com.epam.learn.gymservice.trainee.domain.repository.TraineeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class DeleteTraineeProfile {

  private final TraineeRepository repository;

  public void execute(String username) {
    repository.deleteByUsername(username);
  }
}
