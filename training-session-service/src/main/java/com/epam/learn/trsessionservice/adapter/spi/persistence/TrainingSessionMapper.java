package com.epam.learn.trsessionservice.adapter.spi.persistence;

import com.epam.learn.trsessionservice.adapter.api.rest.dto.TrainingSessionEvent;
import com.epam.learn.trsessionservice.domain.model.TrainingSession;
import com.epam.learn.trsessionservice.domain.model.TrainingSessionMonth;
import com.epam.learn.trsessionservice.domain.model.TrainingSessionYear;
import java.time.LocalDate;
import java.time.LocalTime;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface TrainingSessionMapper {

  TrainingSession toDomain(TrainingSessionEntity entity);

  //  TrainingSession toDomain(TrainingSessionEvent event);

  TrainingSessionEntity toEntity(TrainingSession domain);

  default TrainingSession toDomain(TrainingSessionEvent event) {
    if (event == null) {
      return null;
    }
    TrainingSession session =
        TrainingSession.builder()
            .id(event.id())
            .trainerUsername(event.trainerUsername())
            .trainerFirstName(event.trainerFirstName())
            .trainerLastName(event.trainerLastName())
            .trainerStatus(event.trainerStatus())
            .build();

    session.addYear(buildYear(event.trainingDate(), event.trainingDuration()));

    return session;
  }

  default TrainingSessionYear buildYear(LocalDate trainingDate, LocalTime trainingDuration) {
    TrainingSessionYear year = new TrainingSessionYear(trainingDate.getYear());
    year.addMonth(buildMonth(trainingDate, trainingDuration));
    return year;
  }

  default TrainingSessionMonth buildMonth(
      LocalDate trainingDate, LocalTime trainingDuration) {
    return TrainingSessionMonth.builder()
        .month(trainingDate.getMonth().toString())
        .workload(trainingDuration.toSecondOfDay())
        .build();
  }
}
