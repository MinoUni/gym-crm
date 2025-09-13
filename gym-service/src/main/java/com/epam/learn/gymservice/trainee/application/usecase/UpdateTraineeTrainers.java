package com.epam.learn.gymservice.trainee.application.usecase;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.trainee.domain.repository.TraineeRepository;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.trainer.domain.repository.TrainerRepository;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UpdateTraineeTrainers {

  private final TraineeRepository traineeRepository;
  private final TrainerRepository trainerRepository;

  public Set<Trainer> execute(String traineeUsername, List<String> trainerUsernames) {
    if (trainerRepository.countByUsername(trainerUsernames) != trainerUsernames.size()) {
      throw new EntityNotFoundException(Trainer.class, trainerUsernames);
    }
    Trainee trainee = traineeRepository.getByUsername(traineeUsername);
    List<Trainer> trainers = trainerRepository.findAllByUsernameIn(trainerUsernames);
    trainers.forEach(trainee::addTrainer);
    traineeRepository.update(trainee);
    return trainee.getTrainers();
  }
}
