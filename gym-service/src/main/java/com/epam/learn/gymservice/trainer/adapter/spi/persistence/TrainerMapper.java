package com.epam.learn.gymservice.trainer.adapter.spi.persistence;

import com.epam.learn.gymservice.infra.mapper.MapperConfiguration;
import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.trainer.adapter.api.rest.dto.TrainerCreateRequest;
import com.epam.learn.gymservice.trainer.adapter.api.rest.dto.TrainerProfileResponse;
import com.epam.learn.gymservice.trainer.adapter.api.rest.dto.TrainerUpdateRequest;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.training.domain.model.TrainingType;
import com.epam.learn.gymservice.user.adapter.spi.persistence.UserMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfiguration.class, uses = UserMapper.class)
public interface TrainerMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", source = ".")
  @Mapping(target = "specialization", ignore = true)
  Trainer toEntity(TrainerCreateRequest parameters);

  @Mapping(target = "firstName", source = "user.firstName")
  @Mapping(target = "lastName", source = "user.lastName")
  @Mapping(target = "isActive", source = "user.active")
  TrainerProfileResponse toProfileResponse(Trainer trainer);

  TrainerProfileResponse.Specialization toSpecialization(TrainingType type);

  @Mapping(target = "username", source = "user.username")
  @Mapping(target = "firstName", source = "user.firstName")
  @Mapping(target = "lastName", source = "user.lastName")
  TrainerProfileResponse.TraineeProfile toTraineeProfile(Trainee trainee);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "specialization", ignore = true)
  @Mapping(target = "user", source = ".")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void fullyUpdate(@MappingTarget Trainer trainer, TrainerUpdateRequest parameters);
}
