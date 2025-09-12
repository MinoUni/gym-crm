package com.epam.learn.gymservice.infra.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiErrorResponse {

  @JsonIgnore private final HttpStatusCode httpStatus;
  private final String code;
  private final String message;
  private final List<ApiFieldError> fieldErrors = new ArrayList<>();
  private final List<ApiGlobalError> globalErrors = new ArrayList<>();

  public ApiErrorResponse(HttpStatusCode httpStatus, String code, String message) {
    this.httpStatus = httpStatus;
    this.code = code;
    this.message = message;
  }

  public void addFieldError(ApiFieldError fieldError) {
    fieldErrors.add(fieldError);
  }

  public void addGlobalError(ApiGlobalError globalError) {
    globalErrors.add(globalError);
  }
}
