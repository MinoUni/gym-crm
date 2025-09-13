package com.epam.learn.gymservice.infra.security.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper jacksonMapper;

  @Override
  public void commence(
      HttpServletRequest req, HttpServletResponse resp, AuthenticationException authException)
      throws IOException {
    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
    resp.getWriter()
        .write(
            jacksonMapper.writeValueAsString(
                Map.of(
                    "status",
                    HttpStatus.UNAUTHORIZED.toString(),
                    "message",
                    authException.getMessage())));
  }
}
