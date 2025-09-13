package com.epam.learn.gymservice.user.adapter.spi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.learn.gymservice.trainee.adapter.api.rest.dto.TraineeUpdateRequest;
import com.epam.learn.gymservice.trainer.adapter.api.rest.dto.TrainerUpdateRequest;
import com.epam.learn.gymservice.user.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserMapperTest {

  private final UserMapper mapper = new UserMapperImpl();

  @Test
  @DisplayName("<fullyUpdate> should properly map <TrainerUpdateRequest> into <User>")
  void test_fullyUpdate_shouldMapTrainer() {
    TrainerUpdateRequest parameters = new TrainerUpdateRequest("Duke", "Nuke", Boolean.TRUE, 1L);
    User user = new User(1L, "Godard", "Loke", "Godard.Loke", "pass", false);
    mapper.fullyUpdate(user, parameters);

    assertNotNull(user);
    assertEquals(parameters.firstName(), user.getFirstName());
    assertEquals(parameters.lastName(), user.getLastName());
    assertEquals("Godard.Loke", user.getUsername());
    assertEquals("pass", user.getPassword());
    assertTrue(user.isActive());
  }

  @Test
  @DisplayName("<fullyUpdate> should properly map <TraineeUpdateRequest> into <User>")
  void test_fullyUpdate_shouldMapTrainee() {
    TraineeUpdateRequest parameters =
        new TraineeUpdateRequest("Duke", "Nuke", null, null, Boolean.TRUE);
    User user = new User(1L, "Godard", "Loke", "Godard.Loke", "pass", false);
    mapper.fullyUpdate(user, parameters);

    assertNotNull(user);
    assertEquals(parameters.firstName(), user.getFirstName());
    assertEquals(parameters.lastName(), user.getLastName());
    assertEquals("Godard.Loke", user.getUsername());
    assertEquals("pass", user.getPassword());
    assertTrue(user.isActive());
  }
}
