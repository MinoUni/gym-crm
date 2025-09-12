package com.epam.learn.gymservice.auth.application.usecase;


import com.epam.learn.gymservice.auth.adapter.api.rest.dto.ChangeLoginRequest;
import com.epam.learn.gymservice.infra.exception.SamePasswordException;
import com.epam.learn.gymservice.infra.stereotype.DomainService;
import com.epam.learn.gymservice.user.domain.model.User;
import com.epam.learn.gymservice.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@Transactional
@AllArgsConstructor
public class UserPasswordUpdateUseCase {

  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;

  public void execute(ChangeLoginRequest parameters) {
    User user = repository.getByUsername(parameters.username());
    if (parameters.newPassword().equals(parameters.oldPassword())) {
      throw new SamePasswordException();
    }
    user.setPassword(passwordEncoder.encode(parameters.newPassword()));
    repository.save(user);
  }
}
