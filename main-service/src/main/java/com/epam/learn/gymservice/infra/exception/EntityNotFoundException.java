package com.epam.learn.gymservice.infra.exception;

import java.util.Collection;

public class EntityNotFoundException extends RuntimeException {

  public static final String CODE = "NOT_FOUND";

  public EntityNotFoundException(Class<?> clazz, String username) {
    super(
        "Unable to find <%s> profile with username='%s'"
            .formatted(clazz.getSimpleName(), username));
  }

  public EntityNotFoundException(Class<?> clazz, Long id) {
    super("Unable to find <%s> entity with id='%d'".formatted(clazz.getSimpleName(), id));
  }

  public EntityNotFoundException(Class<?> clazz, Collection<String> usernames) {
    super(
        "Unable to find <%s> profiles with usernames=[%s]"
            .formatted(clazz.getSimpleName(), String.join(",", usernames)));
  }
}
