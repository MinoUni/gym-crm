package com.epam.learn.gymservice.auth.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.learn.gymservice.auth.adapter.api.rest.dto.ChangeLoginRequest;
import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.infra.exception.SamePasswordException;
import com.epam.learn.gymservice.user.domain.model.User;
import com.epam.learn.gymservice.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserPasswordUpdateTest {

  @Mock private UserRepository repository;
  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private UserPasswordUpdate updateUserPassword;

  @Test
  @DisplayName("<execute> should update user's password")
  void test_execute_shouldUpdatePassword() {
    ChangeLoginRequest request = new ChangeLoginRequest("markus.durpe", "pass", "new-pass");
    User user = new User();
    user.setPassword(request.oldPassword());

    when(repository.getByUsername(anyString())).thenReturn(user);
    when(passwordEncoder.encode(anyString())).thenReturn("encoded-new-pass");
    when(repository.save(any(User.class))).thenReturn(user);

    assertDoesNotThrow(() -> updateUserPassword.execute(request));
    assertEquals("encoded-new-pass", user.getPassword());

    verify(repository).getByUsername(request.username());
    verify(passwordEncoder).encode(request.newPassword());
    verify(repository).save(user);
  }

  @Test
  @DisplayName("<execute> should throw <UserNotFoundException>")
  void test_execute_shouldThrowUserNotFoundException() {
    ChangeLoginRequest request = new ChangeLoginRequest("markus.durpe", "pass", "new-pass");

    when(repository.getByUsername(anyString())).thenThrow(EntityNotFoundException.class);

    assertThrows(EntityNotFoundException.class, () -> updateUserPassword.execute(request));

    verify(repository).getByUsername(request.username());
    verify(passwordEncoder, never()).encode(request.newPassword());
    verify(repository, never()).save(any(User.class));
  }

  @Test
  @DisplayName("<execute> should throw <SamePasswordException>")
  void test_execute_shouldThrowSamePasswordException() {
    ChangeLoginRequest request = new ChangeLoginRequest("markus.durpe", "pass", "pass");
    User user = new User();
    user.setPassword(request.oldPassword());

    when(repository.getByUsername(anyString())).thenReturn(user);

    assertThrows(SamePasswordException.class, () -> updateUserPassword.execute(request));

    verify(repository).getByUsername(request.username());
    verify(passwordEncoder, never()).encode(request.newPassword());
    verify(repository, never()).save(any(User.class));
  }
}
