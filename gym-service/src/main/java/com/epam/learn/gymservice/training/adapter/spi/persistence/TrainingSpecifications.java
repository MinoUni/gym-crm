package com.epam.learn.gymservice.training.adapter.spi.persistence;

import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.training.domain.model.Training;
import com.epam.learn.gymservice.training.domain.model.TrainingType;
import com.epam.learn.gymservice.user.domain.model.User;
import jakarta.persistence.criteria.Join;
import java.time.LocalDate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class TrainingSpecifications {

  public static final String LIKE_QUERY_PATTERN = "%%%s%%";

  public Specification<Training> traineeUsernameEquals(String username) {
    return (root, query, criteriaBuilder) -> {
      if (username == null || username.isBlank()) {
        return null;
      }
      Join<Training, Trainee> trainee = root.join("trainee");
      Join<Trainee, User> user = trainee.join("user");
      return criteriaBuilder.equal(user.get("username"), username);
    };
  }

  public Specification<Training> trainerUsernameEquals(String username) {
    return (root, query, criteriaBuilder) -> {
      if (username == null || username.isBlank()) {
        return null;
      }
      Join<Training, Trainer> trainer = root.join("trainer");
      Join<Trainer, User> user = trainer.join("user");
      return criteriaBuilder.equal(user.get("username"), username);
    };
  }

  public Specification<Training> trainerNameLike(String trainerName) {
    return (root, query, criteriaBuilder) -> {
      if (trainerName == null || trainerName.isBlank()) {
        return null;
      }
      Join<Training, Trainer> trainer = root.join("trainer");
      Join<Trainer, User> user = trainer.join("user");
      return criteriaBuilder.like(
          criteriaBuilder.lower(user.get("firstName")),
          LIKE_QUERY_PATTERN.formatted(trainerName.toLowerCase()));
    };
  }

  public Specification<Training> traineeNameLike(String traineeName) {
    return (root, query, criteriaBuilder) -> {
      if (traineeName == null || traineeName.isBlank()) {
        return null;
      }
      Join<Training, Trainee> trainee = root.join("trainee");
      Join<Trainee, User> user = trainee.join("user");
      return criteriaBuilder.like(
          criteriaBuilder.lower(user.get("firstName")),
          LIKE_QUERY_PATTERN.formatted(traineeName.toLowerCase()));
    };
  }

  public Specification<Training> trainingDateBetween(LocalDate start, LocalDate end) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.between(root.get("trainingDate"), start, end);
  }

  public Specification<Training> trainingTypeNameLike(String trainingTypeName) {
    return (root, query, criteriaBuilder) -> {
      if (trainingTypeName == null || trainingTypeName.isBlank()) {
        return null;
      }
      Join<Training, TrainingType> trainingType = root.join("type");
      return criteriaBuilder.like(
          criteriaBuilder.lower(trainingType.get("name")),
          LIKE_QUERY_PATTERN.formatted(trainingTypeName.toLowerCase()));
    };
  }
}
