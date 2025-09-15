package com.epam.learn.trsessionservice.infra.exception;

import org.springframework.validation.FieldError;

public record ApiFieldError(
    String code, String property, String message, Object rejectedValue, String path) {

  public ApiFieldError(FieldError error, String objectName) {
    this(
        error.getCode(),
        error.getField(),
        error.getDefaultMessage(),
        error.getRejectedValue(),
        "%s.%s".formatted(objectName, error.getField()));
  }
}
