package com.epam.learn.gymservice.infra.web.filter;

import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

@Slf4j
@Component
public class RequestLoggingFilter extends AbstractRequestLoggingFilter {

  private static final String REQUEST_ID = "requestId";

  public RequestLoggingFilter() {
    setIncludeClientInfo(true);
    setIncludeQueryString(true);
    setIncludePayload(false);
    setIncludeHeaders(false);
    setBeforeMessagePrefix("Incoming request: ");
    setAfterMessagePrefix("Request completed: ");
  }

  @Override
  protected void beforeRequest(@NonNull HttpServletRequest request, @NonNull String message) {
    MDC.put(REQUEST_ID, "[%s]".formatted(UUID.randomUUID().toString()));
    log.info(message);
  }

  @Override
  protected void afterRequest( @NonNull HttpServletRequest request,@NonNull String message) {
    log.info(message);
    MDC.clear();
  }
}
