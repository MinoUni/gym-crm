package com.epam.learn.gymservice.trainer.application.usecase;

import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.trainer.domain.repository.TrainerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class GetTrainerProfile {

  private final TrainerRepository repository;

  public Trainer execute(String username) {
    return repository.getByUsername(username);
  }
}
