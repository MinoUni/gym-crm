package com.epam.learn.gymservice.user.application.usecase;

import com.epam.learn.gymservice.infra.stereotype.DomainService;
import com.epam.learn.gymservice.user.domain.model.User;
import com.epam.learn.gymservice.user.domain.repository.UserRepository;
import com.epam.learn.gymservice.user.domain.service.UserProfileStatusUpdateService;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@Transactional
@AllArgsConstructor
public class UserProfileStatusUpdateUseCase implements UserProfileStatusUpdateService {

  private final UserRepository repository;

  public void execute(String username, boolean isActive) {
    User user = repository.getByUsername(username);
    user.setActive(isActive);
    repository.save(user);
  }
}
