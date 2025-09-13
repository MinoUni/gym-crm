package com.epam.learn.gymservice.auth.adapter.api.rest.dto;

import com.epam.learn.gymservice.user.domain.model.User;
import jakarta.validation.constraints.NotBlank;

public record AuthCredentials(@NotBlank String username, @NotBlank String password) {

  public static AuthCredentials of(User user) {
    return new AuthCredentials(user.getUsername(), user.getPassword());
  }
}
