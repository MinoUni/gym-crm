package com.epam.learn.gymservice.training.domain.repository;

import com.epam.learn.gymservice.training.domain.model.Training;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public interface TrainingRepository {

  Training save(Training training);

  List<Training> findAll(Specification<Training> specification);
}
