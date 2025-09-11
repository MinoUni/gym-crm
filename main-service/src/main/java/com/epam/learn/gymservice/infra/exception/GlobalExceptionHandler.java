package com.epam.learn.gymservice.infra.exception;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ApiErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    var response =
        new ApiErrorResponse(
            e.getStatusCode(),
            "VALIDATION_FAILED",
            "Validation failed for obj='%s'. Error count: %d"
                .formatted(
                    e.getBindingResult().getObjectName(), e.getBindingResult().getErrorCount()));

    e.getBindingResult()
        .getFieldErrors()
        .forEach(
            error -> {
              var apiFieldError =
                  new ApiFieldError(
                      error.getCode(),
                      error.getField(),
                      error.getDefaultMessage(),
                      error.getRejectedValue(),
                      "%s.%s".formatted(e.getBindingResult().getObjectName(), error.getField()));
              response.addFieldError(apiFieldError);
            });
    e.getBindingResult()
        .getGlobalErrors()
        .forEach(
            error -> {
              String defaultCode =
                  Optional.ofNullable(error.getCodes())
                      .filter(codes -> codes.length > 0)
                      .map(codes -> codes[codes.length - 1])
                      .orElse(null);
              var apiGlobalError = new ApiGlobalError(defaultCode, error.getDefaultMessage());
              response.addGlobalError(apiGlobalError);
            });
    return response;
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public ApiErrorResponse handleEntityNotFoundException(EntityNotFoundException e) {
    return new ApiErrorResponse(HttpStatus.NOT_FOUND, EntityNotFoundException.CODE, e.getMessage());
  }
}
