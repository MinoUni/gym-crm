package com.epam.learn.trsessionservice.adapter.spi.persistence;

import com.epam.learn.trsessionservice.adapter.api.rest.dto.TrainingSessionEvent;
import com.epam.learn.trsessionservice.domain.model.TrainingSession;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface TrainingSessionMapper {

  TrainingSession toDomain(TrainingSessionEntity entity);

  TrainingSession toDomain(TrainingSessionEvent event);

  TrainingSessionEntity toEntity(TrainingSession domain);
}
