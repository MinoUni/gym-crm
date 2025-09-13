package com.epam.learn.gymservice.auth.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.learn.gymservice.auth.adapter.api.rest.dto.AuthCredentials;
import com.epam.learn.gymservice.infra.security.jwt.JwtProvider;
import com.epam.learn.gymservice.infra.security.userdetails.SecurityUser;
import com.epam.learn.gymservice.user.domain.model.User;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserTest {

  @Mock private JwtProvider jwtProvider;
  @Mock private AuthenticationManager authenticationManager;

  @InjectMocks private AuthenticateUser authenticateUser;

  @Captor ArgumentCaptor<Map<String, Object>> claimsCaptor;

  @Test
  @DisplayName("<execute> should generate JWT token with proper claims")
  void test_execute_shouldGenerateJwtTokenWithProperClaims() {
    AuthCredentials credentials = new AuthCredentials("John.Pork", "password");
    User user = new User();
    user.setUsername(credentials.username());
    user.setPassword(credentials.password());
    user.setFirstName("John");
    user.setLastName("Pork");
    SecurityUser securityUser = new SecurityUser(user, List.of(() -> "USER"));
    var authentication =
        new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
    String jwt = "jwt-token";

    when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
    when(jwtProvider.generateAccessToken(anyMap())).thenReturn(jwt);

    String result = assertDoesNotThrow(() -> authenticateUser.execute(credentials));

    assertEquals(jwt, result);
    verify(authenticationManager).authenticate(any(Authentication.class));
    verify(jwtProvider).generateAccessToken(claimsCaptor.capture());
    Map<String, Object> claims = claimsCaptor.getValue();
    assertEquals(user.getUsername(), claims.get("username"));
    assertEquals(user.getFirstName(), claims.get("firstName"));
    assertEquals(user.getLastName(), claims.get("lastName"));
    assertEquals(List.of("USER"), claims.get("roles"));
  }

  @Test
  @DisplayName("<execute> should throw <BadCredentialsException> when authentication fails")
  void test_execute_shouldThrowBadCredentialsException_whenAuthenticationFails() {
    AuthCredentials credentials = new AuthCredentials("John.Pork", "password");

    when(authenticationManager.authenticate(any(Authentication.class)))
        .thenThrow(BadCredentialsException.class);

    assertThrows(AuthenticationException.class, () -> authenticateUser.execute(credentials));

    verify(authenticationManager).authenticate(any(Authentication.class));
    verify(jwtProvider, never()).generateAccessToken(anyMap());
  }
}
