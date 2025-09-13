package com.epam.learn.gymservice.training.adapter.spi.persistence;

import com.epam.learn.gymservice.training.domain.model.Training;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTrainingRepository
    extends BaseJpaRepository<Training, Long>, JpaSpecificationExecutor<Training> {}
