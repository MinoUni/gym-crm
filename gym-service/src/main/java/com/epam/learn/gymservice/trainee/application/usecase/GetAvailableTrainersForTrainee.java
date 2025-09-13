package com.epam.learn.gymservice.trainee.application.usecase;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.trainee.domain.repository.TraineeRepository;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.trainer.domain.repository.TrainerRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class GetAvailableTrainersForTrainee {

  private final TraineeRepository traineeRepository;
  private final TrainerRepository trainerRepository;

  public List<Trainer> execute(String username) {
    if (!traineeRepository.existsByUsername(username)) {
      throw new EntityNotFoundException(Trainee.class, username);
    }
    return trainerRepository.findActiveTrainersNotAssignedToTrainee(username);
  }
}
