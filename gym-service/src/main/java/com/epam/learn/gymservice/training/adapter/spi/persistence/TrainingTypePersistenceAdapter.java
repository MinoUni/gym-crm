package com.epam.learn.gymservice.training.adapter.spi.persistence;

import com.epam.learn.gymservice.training.domain.model.TrainingType;
import com.epam.learn.gymservice.training.domain.repository.TrainingTypeRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TrainingTypePersistenceAdapter implements TrainingTypeRepository {

  private final JpaTrainingTypeRepository repository;

  @Override
  public List<TrainingType> findAll() {
    return repository.findAll();
  }

  @Override
  public TrainingType getReferenceById(Long id) {
    return repository.getReferenceById(id);
  }

  @Override
  public boolean existsById(Long id) {
    return repository.existsById(id);
  }
}
