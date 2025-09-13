package com.epam.learn.gymservice.trainee.adapter.api.rest.dto;

import com.epam.learn.gymservice.infra.mapper.UserMappings;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public record TraineeCreateRequest(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotBlank String password,
    @Past LocalDate birthDate,
    String address)
    implements UserMappings {}
