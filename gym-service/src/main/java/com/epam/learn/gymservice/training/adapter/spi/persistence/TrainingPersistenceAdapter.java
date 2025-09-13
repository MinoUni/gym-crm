package com.epam.learn.gymservice.training.adapter.spi.persistence;

import com.epam.learn.gymservice.training.domain.model.Training;
import com.epam.learn.gymservice.training.domain.repository.TrainingRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TrainingPersistenceAdapter implements TrainingRepository {

  private final JpaTrainingRepository repository;

  @Override
  public Training save(Training training) {
    return repository.persist(training);
  }

  @Override
  public List<Training> findAll(Specification<Training> specification) {
    return repository.findAll(specification);
  }
}
