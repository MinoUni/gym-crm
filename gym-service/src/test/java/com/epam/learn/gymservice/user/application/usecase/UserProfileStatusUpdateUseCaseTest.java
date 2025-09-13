package com.epam.learn.gymservice.user.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.user.domain.model.User;
import com.epam.learn.gymservice.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserProfileStatusUpdateUseCaseTest {

  public static final String USERNAME = "Julia";

  @Mock private UserRepository repository;
  @InjectMocks private UserProfileStatusUpdateUseCase updateUserProfileStatus;

  @Test
  @DisplayName("<execute> should update profile 'isActive' status")
  void test_execute_shouldUpdateStatus() {
    User user = new User();

    when(repository.getByUsername(USERNAME)).thenReturn(user);
    when(repository.save(user)).thenReturn(user);

    assertDoesNotThrow(() -> updateUserProfileStatus.execute(USERNAME, true));
    assertTrue(user.isActive());

    verify(repository).getByUsername(USERNAME);
    verify(repository).save(user);
  }

  @Test
  @DisplayName("<execute> should throw <UserNotFoundException>")
  void test_execute_shouldThrowUserNotFoundException() {

    when(repository.getByUsername(USERNAME)).thenThrow(EntityNotFoundException.class);

    assertThrows(
        EntityNotFoundException.class, () -> updateUserProfileStatus.execute(USERNAME, false));

    verify(repository).getByUsername(USERNAME);
    verify(repository, never()).save(any(User.class));
  }

  @Test
  @DisplayName("<execute> should be <IDEMPOTENT> action")
  void test_execute_shouldBeIdempotent() {
    User user = new User();
    user.setActive(true);

    when(repository.getByUsername(USERNAME)).thenReturn(user);
    when(repository.save(user)).thenReturn(user);

    assertDoesNotThrow(() -> updateUserProfileStatus.execute(USERNAME, true));
    assertTrue(user.isActive());

    verify(repository).getByUsername(USERNAME);
    verify(repository).save(user);
  }
}
