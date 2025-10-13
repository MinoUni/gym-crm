package com.epam.learn.gymservice.training.domain.repository;

import com.epam.learn.gymservice.training.domain.model.TrainingType;
import java.util.List;

public interface TrainingTypeRepository {

  List<TrainingType> findAll();

  TrainingType getReferenceById(Long id);

  boolean existsById(Long id);

  TrainingType save(TrainingType trainingType);
}
