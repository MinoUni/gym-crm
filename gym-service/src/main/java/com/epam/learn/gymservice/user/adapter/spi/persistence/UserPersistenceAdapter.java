package com.epam.learn.gymservice.user.adapter.spi.persistence;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.user.domain.model.User;
import com.epam.learn.gymservice.user.domain.repository.UserRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserPersistenceAdapter implements UserRepository {

  private final JpaUserRepository repository;

  @Override
  public User save(User user) {
    return repository.persist(user);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return repository.findByUsername(username);
  }

  @Override
  public User getByUsername(String username) {
    return repository
        .findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException(User.class, username));
  }
}
