package com.epam.learn.gymservice.training.adapter.spi.persistence;

import com.epam.learn.gymservice.training.domain.model.TrainingType;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface JpaTrainingTypeRepository extends BaseJpaRepository<TrainingType, Long> {

  @Query("FROM TrainingType")
  List<TrainingType> findAll();
}
