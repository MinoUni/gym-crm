package com.epam.learn.trsessionservice.infra.exception;

public class TrainingSessionNotFoundException extends RuntimeException {

  public TrainingSessionNotFoundException(Long id) {
    super("Training session with 'id=%d' not found".formatted(id));
  }
}
