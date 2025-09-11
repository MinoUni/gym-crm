package com.epam.learn.gymservice.infra.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UsernameGeneratorUtils {

  public String generateUsername(String firstName, String lastName) {
    String baseUsername = "%s.%s".formatted(firstName, lastName);
    String uniqueSuffix = String.valueOf(System.currentTimeMillis() % 10_000);
    return "%s-%s".formatted(baseUsername, uniqueSuffix);
  }
}
