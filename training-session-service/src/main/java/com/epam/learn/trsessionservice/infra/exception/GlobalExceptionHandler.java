package com.epam.learn.trsessionservice.infra.exception;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
    List<ApiGlobalError> globalErrors = extractGlobalErrors(e);
    List<ApiFieldError> fieldErrors = extractFieldErrors(e);
    String detail =
        "Validation failed for obj='%s'. Error count: %d"
            .formatted(e.getBindingResult().getObjectName(), e.getBindingResult().getErrorCount());
    return ErrorResponse.builder(e, ProblemDetail.forStatusAndDetail(e.getStatusCode(), detail))
        .property("globalErrors", globalErrors)
        .property("fieldErrors", fieldErrors)
        .build();
  }

  @ExceptionHandler(TrainingSessionNotFoundException.class)
  public ErrorResponse handleTrainingSessionNotFoundException(TrainingSessionNotFoundException e) {
    return ErrorResponse.builder(
            e, ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage()))
        .build();
  }

  private List<ApiFieldError> extractFieldErrors(MethodArgumentNotValidException e) {
    return e.getBindingResult().getFieldErrors().stream()
        .map(error -> new ApiFieldError(error, e.getBindingResult().getObjectName()))
        .toList();
  }

  private List<ApiGlobalError> extractGlobalErrors(MethodArgumentNotValidException e) {
    return e.getBindingResult().getGlobalErrors().stream()
        .map(
            error -> {
              String defaultCode =
                  Optional.ofNullable(error.getCodes())
                      .filter(codes -> codes.length > 0)
                      .map(codes -> codes[codes.length - 1])
                      .orElse(null);
              return new ApiGlobalError(defaultCode, error.getDefaultMessage());
            })
        .toList();
  }
}
