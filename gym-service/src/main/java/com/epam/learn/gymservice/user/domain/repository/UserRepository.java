package com.epam.learn.gymservice.user.domain.repository;

import com.epam.learn.gymservice.user.domain.model.User;
import java.util.Optional;

public interface UserRepository {

  User save(User user);

  Optional<User> findByUsername(String username);

  User getByUsername(String username);
}
