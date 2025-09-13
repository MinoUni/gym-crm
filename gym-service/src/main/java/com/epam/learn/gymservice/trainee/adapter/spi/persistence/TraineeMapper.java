package com.epam.learn.gymservice.trainee.adapter.spi.persistence;

import com.epam.learn.gymservice.infra.mapper.MapperConfiguration;
import com.epam.learn.gymservice.trainee.adapter.api.rest.dto.TraineeCreateRequest;
import com.epam.learn.gymservice.trainee.adapter.api.rest.dto.TraineeProfileResponse;
import com.epam.learn.gymservice.trainee.adapter.api.rest.dto.TraineeUpdateRequest;
import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.user.adapter.spi.persistence.UserMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfiguration.class, uses = UserMapper.class)
public interface TraineeMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", source = ".")
  Trainee toEntity(TraineeCreateRequest parameters);

  @Mapping(target = "username", source = "user.username")
  @Mapping(target = "firstName", source = "user.firstName")
  @Mapping(target = "lastName", source = "user.lastName")
  @Mapping(target = "isActive", source = "user.active")
  TraineeProfileResponse toProfileResponse(Trainee entity);

  @Mapping(target = "username", source = "user.username")
  @Mapping(target = "firstName", source = "user.firstName")
  @Mapping(target = "lastName", source = "user.lastName")
  @Mapping(target = "specialization", source = "specialization.name")
  TraineeProfileResponse.TrainerProfile toTrainerProfile(Trainer entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", source = ".")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void fullyUpdate(@MappingTarget Trainee trainee, TraineeUpdateRequest parameters);
}
