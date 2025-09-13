package com.epam.learn.gymservice.trainee.application.usecase;

import com.epam.learn.gymservice.trainee.adapter.api.rest.dto.TraineeCreateRequest;
import com.epam.learn.gymservice.trainee.adapter.spi.persistence.TraineeMapper;
import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.trainee.domain.repository.TraineeRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class CreateTrainee {

  private final TraineeMapper mapper;
  private final TraineeRepository repository;
  private final PasswordEncoder passwordEncoder;

  public Trainee execute(TraineeCreateRequest parameters) {
    Trainee trainee = mapper.toEntity(parameters);
    trainee.getUser().setPassword(passwordEncoder.encode(parameters.password()));
    return repository.save(trainee);
  }
}
