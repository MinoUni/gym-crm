package com.epam.learn.gymservice.infra.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;

@UtilityClass
public class UsernameGeneratorUtils {

  public String generateUsername(String firstName, String lastName) {
    String baseUsername = "%s.%s".formatted(firstName, lastName);
    return "%s-%s".formatted(baseUsername, RandomStringUtils.insecure().next(3, true, true));
  }
}
