package com.epam.learn.gymservice.user.adapter.spi.persistence;

import com.epam.learn.gymservice.infra.mapper.MapperConfiguration;
import com.epam.learn.gymservice.infra.mapper.UserMappings;
import com.epam.learn.gymservice.infra.mapper.UserUpdateMappings;
import com.epam.learn.gymservice.infra.utils.UsernameGeneratorUtils;
import com.epam.learn.gymservice.user.domain.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper {

  @Named("generateUsername")
  default String generateUsername(UserMappings parameters) {
    return UsernameGeneratorUtils.generateUsername(parameters.firstName(), parameters.lastName());
  }

  @Named("toFullName")
  default String toFullName(User user) {
    if (user == null) return null;
    String firstName = user.getFirstName().isEmpty() ? "" : user.getFirstName();
    String lastName = user.getLastName().isEmpty() ? "" : user.getLastName();
    return "%s %s".formatted(firstName, lastName);
  }

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "lastName", expression = "java(parameters.lastName())")
  @Mapping(target = "firstName", expression = "java(parameters.firstName())")
  @Mapping(target = "username", source = ".", qualifiedByName = "generateUsername")
  User toEntity(UserMappings parameters);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "username", ignore = true)
  @Mapping(target = "firstName", expression = "java(parameters.firstName())")
  @Mapping(target = "lastName", expression = "java(parameters.lastName())")
  @Mapping(target = "active", source = "active")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void fullyUpdate(@MappingTarget User entity, UserUpdateMappings parameters);
}
