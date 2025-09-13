package com.epam.learn.gymservice.training.application.usecase;

import com.epam.learn.gymservice.infra.stereotype.DomainService;
import com.epam.learn.gymservice.training.adapter.api.rest.dto.TrainingTypeResponse;
import com.epam.learn.gymservice.training.adapter.spi.persistence.TrainingMapper;
import com.epam.learn.gymservice.training.domain.repository.TrainingTypeRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@AllArgsConstructor
@Transactional(readOnly = true)
public class GetTrainingTypesUseCase {

  private final TrainingTypeRepository repository;
  private final TrainingMapper mapper;

  public List<TrainingTypeResponse> execute() {
    return repository.findAll().stream().map(mapper::toTrainingTypeResponse).toList();
  }
}
