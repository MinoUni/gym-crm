package com.epam.learn.gymservice.user.adapter.spi.persistence;

import com.epam.learn.gymservice.user.domain.model.User;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends BaseJpaRepository<User, Long> {

  Optional<User> findByUsername(String username);
}
