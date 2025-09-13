package com.epam.learn.gymservice.auth.adapter.api.rest;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.learn.gymservice.auth.adapter.api.rest.dto.AuthCredentials;
import com.epam.learn.gymservice.auth.adapter.api.rest.dto.ChangeLoginRequest;
import com.epam.learn.gymservice.auth.application.usecase.AuthenticateUser;
import com.epam.learn.gymservice.auth.application.usecase.UserPasswordUpdate;
import com.epam.learn.gymservice.infra.exception.SamePasswordException;
import com.epam.learn.gymservice.infra.security.WebSecurityConfig;
import com.epam.learn.gymservice.infra.security.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@Import({WebSecurityConfig.class, JwtProvider.class})
class AuthControllerTest {

  @MockitoBean private AuthenticateUser authenticateUser;
  @MockitoBean private UserPasswordUpdate updateUserPassword;

  @Autowired private ObjectMapper jackson;
  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("<authenticate> should generate JWT with <200 OK>")
  void test_authenticate_shouldGenerateJwt_with200StatusCode() throws Exception {
    String jwt = "jwt-token";
    AuthCredentials authentication = new AuthCredentials("John.Carlson", "pass");

    when(authenticateUser.execute(any(AuthCredentials.class))).thenReturn(jwt);

    mockMvc
        .perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jackson.writeValueAsString(authentication)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.token").value(jwt));

    verify(authenticateUser).execute(authentication);
  }

  @Test
  @DisplayName("<authenticate> should fail validation with <400 BAD_REQUEST>")
  void test_authenticate_shouldFailValidation_with400StatusCode() throws Exception {
    AuthCredentials authentication = new AuthCredentials("", null);

    mockMvc
        .perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jackson.writeValueAsString(authentication)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
        .andExpect(
            jsonPath("$.message")
                .value("Validation failed for obj='authCredentials'. Error count: 2"))
        .andExpect(jsonPath("$.fieldErrors", hasSize(2)))
        .andExpect(
            jsonPath("$.fieldErrors[*].property", containsInAnyOrder("username", "password")));

    verify(authenticateUser, never()).execute(authentication);
  }

  @Test
  @DisplayName("<authenticate> should throw <BadCredentialsException> with <401 UNAUTHORIZED>")
  void test_authenticate_shouldThrowBadCredentialsException_with401StatusCode() throws Exception {
    AuthCredentials authentication = new AuthCredentials("tirex", "pass");

    when(authenticateUser.execute(any(AuthCredentials.class)))
        .thenThrow(BadCredentialsException.class);

    mockMvc
        .perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jackson.writeValueAsString(authentication)))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value("BAD_CREDENTIALS"));

    verify(authenticateUser).execute(authentication);
  }

  @Test
  @DisplayName("<changeLogin> should update password with <204 NO_CONTENT>")
  void test_changeLogin_shouldUpdatePassword_with204StatusCode() throws Exception {
    ChangeLoginRequest request = new ChangeLoginRequest("markus.durpe", "pass", "new-pass");

    doNothing().when(updateUserPassword).execute(any(ChangeLoginRequest.class));

    mockMvc
        .perform(
            put("/login")
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jackson.writeValueAsString(request)))
        .andExpect(status().isNoContent());

    verify(updateUserPassword).execute(request);
  }

  @Test
  @DisplayName("<changeLogin> should throw <SamePasswordException> with <400 BAD_REQUEST>")
  void test_changeLogin_shouldThrowSamePasswordException_with400StatusCode() throws Exception {
    ChangeLoginRequest request = new ChangeLoginRequest("markus.durpe", "pass", "new-pass");

    doThrow(SamePasswordException.class)
        .when(updateUserPassword)
        .execute(any(ChangeLoginRequest.class));

    mockMvc
        .perform(
            put("/login")
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jackson.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value("PASSWORD_REUSE"));

    verify(updateUserPassword).execute(request);
  }

  @Test
  @DisplayName("<changeLogin> should fail validation with <400 BAD_REQUEST>")
  void test_changeLogin_shouldFailValidation_with400StatusCode() throws Exception {
    ChangeLoginRequest request = new ChangeLoginRequest(null, "", "   ");

    mockMvc
        .perform(
            put("/login")
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jackson.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
        .andExpect(
            jsonPath("$.message")
                .value("Validation failed for obj='changeLoginRequest'. Error count: 3"))
        .andExpect(jsonPath("$.fieldErrors", hasSize(3)))
        .andExpect(
            jsonPath(
                "$.fieldErrors[*].property",
                containsInAnyOrder("username", "oldPassword", "newPassword")));

    verify(updateUserPassword, never()).execute(request);
  }

  @Test
  @DisplayName("<changeLogin> should return <401 UNAUTHORIZED> when no JWT provided")
  void test_changeLogin_shouldReturn401StatusCode_whenNoJwtProvided() throws Exception {
    ChangeLoginRequest request = new ChangeLoginRequest("markus.durpe", "pass", "new-pass");

    mockMvc
        .perform(
            put("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jackson.writeValueAsString(request)))
        .andExpect(status().isUnauthorized());

    verify(updateUserPassword, never()).execute(request);
  }
}
