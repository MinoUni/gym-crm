package com.epam.learn.gymservice.training.adapter.spi.persistence;

import com.epam.learn.gymservice.infra.mapper.MapperConfiguration;
import com.epam.learn.gymservice.trainee.adapter.api.rest.dto.TraineeTrainingResponse;
import com.epam.learn.gymservice.trainer.adapter.api.rest.dto.TrainerTrainingResponse;
import com.epam.learn.gymservice.trainer.adapter.spi.persistence.TrainerMapper;
import com.epam.learn.gymservice.training.adapter.api.rest.dto.TrainingCreateRequest;
import com.epam.learn.gymservice.training.adapter.api.rest.dto.TrainingTypeResponse;
import com.epam.learn.gymservice.training.domain.model.Training;
import com.epam.learn.gymservice.training.domain.model.TrainingType;
import com.epam.learn.gymservice.user.adapter.spi.persistence.UserMapper;
import java.time.Duration;
import java.time.LocalTime;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
    config = MapperConfiguration.class,
    uses = {TrainerMapper.class, UserMapper.class})
public interface TrainingMapper {

  @Named("toFormattedDuration")
  default String formatDuration(Duration duration) {
    if (duration == null) {
      return null;
    }
    return DurationFormatUtils.formatDuration(duration.toMillis(), "HH:mm:ss", true);
  }

  @Named("toDuration")
  default Duration toDuration(LocalTime duration) {
    if (duration == null) {
      return null;
    }
    return Duration.between(LocalTime.MIN, duration);
  }

  @Mapping(target = "trainingType", source = "type.name")
  @Mapping(target = "trainerName", source = "trainer.user", qualifiedByName = "toFullName")
  @Mapping(target = "duration", source = "duration", qualifiedByName = "toFormattedDuration")
  TraineeTrainingResponse toTraineeTrainingResponse(Training training);

  @Mapping(target = "trainingType", source = "type.name")
  @Mapping(target = "traineeName", source = "trainee.user", qualifiedByName = "toFullName")
  @Mapping(target = "duration", source = "duration", qualifiedByName = "toFormattedDuration")
  TrainerTrainingResponse toTrainerTrainingResponse(Training training);

  TrainingTypeResponse toTrainingTypeResponse(TrainingType trainingType);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "type", ignore = true)
  @Mapping(target = "trainer", ignore = true)
  @Mapping(target = "trainee", ignore = true)
  @Mapping(target = "trainingDate", source = "date")
  @Mapping(target = "duration", source = "duration", qualifiedByName = "toDuration")
  Training toEntity(TrainingCreateRequest parameters);
}
