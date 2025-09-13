package com.epam.learn.gymservice.trainee.adapter.spi.persistence;

import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaTraineeRepository extends BaseJpaRepository<Trainee, Long> {

  @EntityGraph(attributePaths = {"user", "trainers", "trainers.specialization", "trainers.user"})
  Optional<Trainee> findByUser_Username(String username);

  void deleteByUser_Username(String username);

  boolean existsByUser_Username(String username);

  @Query(
      """
        SELECT t.id
        FROM Trainee t
        WHERE t.user.username = :username
      """)
  Optional<Long> findIdByUsername(@Param("username") String username);
}
