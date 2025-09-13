package com.epam.learn.gymservice.trainer.adapter.spi.persistence;

import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaTrainerRepository extends BaseJpaRepository<Trainer, Long> {

  @EntityGraph(attributePaths = {"user", "specialization", "trainees", "trainees.user"})
  Optional<Trainer> findByUser_Username(String username);

  @Query(
      """
              SELECT t FROM Trainer t
              JOIN FETCH t.user tu
              JOIN FETCH t.specialization
              WHERE tu.isActive = true
                AND NOT EXISTS (
                  SELECT 1 FROM Trainee trainee
                  JOIN trainee.trainers tr
                  JOIN trainee.user tru
                  WHERE tru.username = :username AND tr = t
                )
            """)
  List<Trainer> findActiveTrainersNotAssignedToTrainee(@Param("username") String traineeUsername);

  @EntityGraph(attributePaths = {"user", "specialization"})
  List<Trainer> findAllByUser_UsernameIn(Collection<String> usernames);

  @Query(
      """
              SELECT COUNT(tu)
              FROM Trainer t
              JOIN t.user tu
              WHERE tu.isActive = true AND tu.username IN :usernames
            """)
  long countAllByUser_Username(@Param("usernames") Collection<String> usernames);

  @Query(
      """
              SELECT t.id
              FROM Trainer t
              WHERE t.user.username = :username
            """)
  Optional<Long> findIdByUsername(@Param("username") String username);

  boolean existsByUser_Username(String username);
}
