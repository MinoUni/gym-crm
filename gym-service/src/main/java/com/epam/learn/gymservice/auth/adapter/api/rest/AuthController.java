package com.epam.learn.gymservice.auth.adapter.api.rest;

import com.epam.learn.gymservice.auth.adapter.api.rest.dto.AuthCredentials;
import com.epam.learn.gymservice.auth.adapter.api.rest.dto.ChangeLoginRequest;
import com.epam.learn.gymservice.auth.application.usecase.AuthenticateUserUseCase;
import com.epam.learn.gymservice.auth.application.usecase.UserPasswordUpdateUseCase;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/login")
public class AuthController {

  private final AuthenticateUserUseCase authenticateUserUseCase;
  private final UserPasswordUpdateUseCase userPasswordUpdateUseCase;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public Map<String, Object> authenticate(@Valid @RequestBody AuthCredentials authentication) {
    String token = authenticateUserUseCase.execute(authentication);
    return Map.of("token", token);
  }

  @PutMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void changeLogin(@Valid @RequestBody ChangeLoginRequest request) {
    userPasswordUpdateUseCase.execute(request);
  }
}
