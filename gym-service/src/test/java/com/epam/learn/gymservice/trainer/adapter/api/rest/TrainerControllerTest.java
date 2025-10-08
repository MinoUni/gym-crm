package com.epam.learn.gymservice.trainer.adapter.api.rest;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.infra.security.WebSecurityConfig;
import com.epam.learn.gymservice.infra.security.web.JwtAuthenticationEntryPoint;
import com.epam.learn.gymservice.trainer.adapter.api.rest.dto.TrainerCreateRequest;
import com.epam.learn.gymservice.trainer.adapter.api.rest.dto.TrainerProfileResponse;
import com.epam.learn.gymservice.trainer.adapter.api.rest.dto.TrainerTrainingResponse;
import com.epam.learn.gymservice.trainer.adapter.api.rest.dto.TrainerUpdateRequest;
import com.epam.learn.gymservice.trainer.adapter.spi.persistence.TrainerMapper;
import com.epam.learn.gymservice.trainer.application.usecase.CreateTrainer;
import com.epam.learn.gymservice.trainer.application.usecase.GetTrainerProfile;
import com.epam.learn.gymservice.trainer.application.usecase.GetTrainerTrainings;
import com.epam.learn.gymservice.trainer.application.usecase.UpdateTrainerProfile;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.user.application.usecase.UserProfileStatusUpdateUseCase;
import com.epam.learn.gymservice.user.domain.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TrainerController.class)
@Import({WebSecurityConfig.class, JwtAuthenticationEntryPoint.class})
class TrainerControllerTest {

  @MockitoBean private TrainerMapper mapper;
  @MockitoBean private CreateTrainer createTrainer;
  @MockitoBean private GetTrainerProfile getTrainerProfile;
  @MockitoBean private GetTrainerTrainings getTrainerTrainings;
  @MockitoBean private UpdateTrainerProfile updateTrainerProfile;
  @MockitoBean private UserProfileStatusUpdateUseCase updateTrainerProfileStatus;

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper jackson;

  @Test
  @DisplayName("<getTrainerProfile> should return <401 UNAUTHORIZED> when no JWT provided")
  void test_getTrainerProfile_shouldReturn401StatusCode_whenNoJwtProvided() throws Exception {
    String username = "John.Doe";

    mockMvc
        .perform(get("/trainers/{username}", username))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.toString()))
        .andExpect(
            jsonPath("$.message").value("Full authentication is required to access this resource"));

    verify(getTrainerProfile, never()).execute(anyString());
    verify(mapper, never()).toProfileResponse(any(Trainer.class));
  }

  @Test
  @DisplayName("<getTrainerProfile> should get trainer profile details with <200 OK>")
  void test_getTrainerProfile_shouldReturnTrainerProfile_with200StatusCode() throws Exception {
    String username = "John.Doe";
    Trainer trainer = new Trainer();
    TrainerProfileResponse.Specialization specialization =
        new TrainerProfileResponse.Specialization(1L, "Cardio");
    TrainerProfileResponse profileResp =
        new TrainerProfileResponse("John", "Doe", true, specialization, List.of());

    when(getTrainerProfile.execute(anyString())).thenReturn(trainer);
    when(mapper.toProfileResponse(any(Trainer.class))).thenReturn(profileResp);

    mockMvc
        .perform(
            get("/trainers/{username}", username).with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.firstName", is(profileResp.firstName())))
        .andExpect(jsonPath("$.lastName", is(profileResp.lastName())))
        .andExpect(jsonPath("$.specialization.id", is(specialization.id().intValue())))
        .andExpect(jsonPath("$.specialization.name", is(specialization.name())))
        .andExpect(jsonPath("$.trainees", hasSize(0)));

    verify(getTrainerProfile).execute(username);
    verify(mapper).toProfileResponse(trainer);
  }

  @Test
  @DisplayName("<getTrainerProfile> should throw <EntityNotFoundException> with <404 NOT_FOUND>")
  void test_getTrainerProfile_shouldThrowEntityNotFoundException_with404StatusCode()
      throws Exception {
    String username = "John.Doe";

    when(getTrainerProfile.execute(username))
        .thenThrow(new EntityNotFoundException(Trainer.class, username));

    mockMvc
        .perform(
            get("/trainers/{username}", username).with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.name()))
        .andExpect(
            jsonPath("$.message")
                .value(
                    "Unable to find <%s> profile with username='%s'"
                        .formatted(Trainer.class.getSimpleName(), username)));

    verify(getTrainerProfile).execute(username);
    verify(mapper, never()).toProfileResponse(any(Trainer.class));
  }

  @Test
  @DisplayName("<createTrainer> should fail validation with <400 BAD_REQUEST>")
  void test_createTrainer_shouldFailValidation_with400StatusCode() throws Exception {
    TrainerCreateRequest request = new TrainerCreateRequest("", null, null);

    mockMvc
        .perform(
            post("/trainers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jackson.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
        .andExpect(
            jsonPath("$.message")
                .value("Validation failed for obj='trainerCreateRequest'. Error count: 3"))
        .andExpect(jsonPath("$.fieldErrors", hasSize(3)))
        .andExpect(
            jsonPath(
                "$.fieldErrors[*].property",
                containsInAnyOrder("firstName", "lastName", "trainingTypeId")));

    verify(createTrainer, never()).execute(request);
  }

  @Test
  @DisplayName("<createTrainer> should create trainer and return credentials with <201 CREATED>")
  void test_createTrainer_shouldCreateTrainer_with201StatusCode() throws Exception {
    TrainerCreateRequest request = new TrainerCreateRequest("John", "Doe", 1L);
    Trainer trainer = new Trainer();
    trainer.setUser(
        new User(null, request.firstName(), request.lastName(), "John.Doe", false));

    when(createTrainer.execute(any(TrainerCreateRequest.class))).thenReturn(trainer);

    mockMvc
        .perform(
            post("/trainers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jackson.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.username", is("John.Doe")))
        .andExpect(jsonPath("$.password").exists());

    verify(createTrainer).execute(request);
  }

  @Test
  @DisplayName("<updateTrainer> should return <401 UNAUTHORIZED> when no JWT provided")
  void test_updateTrainer_shouldReturn401StatusCode_whenNoJwtProvided() throws Exception {
    String username = "John.Smith";
    TrainerUpdateRequest request = new TrainerUpdateRequest("Ada", "Wong", true, 1L);

    mockMvc
        .perform(
            put("/trainers/{username}", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jackson.writeValueAsString(request)))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.toString()))
        .andExpect(
            jsonPath("$.message").value("Full authentication is required to access this resource"));

    verify(updateTrainerProfile, never()).execute(username, request);
    verify(mapper, never()).toProfileResponse(any(Trainer.class));
  }

  @Test
  @DisplayName("<updateTrainer> should fail validation with <400 BAD_REQUEST>")
  void test_updateTrainer_shouldFailValidation_400StatusCode() throws Exception {
    String username = "John.Smith";
    TrainerUpdateRequest request = new TrainerUpdateRequest(null, " ", null, -1L);

    mockMvc
        .perform(
            put("/trainers/{username}", username)
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jackson.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
        .andExpect(
            jsonPath("$.message")
                .value("Validation failed for obj='trainerUpdateRequest'. Error count: 4"))
        .andExpect(jsonPath("$.fieldErrors", hasSize(4)))
        .andExpect(
            jsonPath(
                "$.fieldErrors[*].property",
                containsInAnyOrder("firstName", "lastName", "specializationRef", "isActive")));

    verify(updateTrainerProfile, never()).execute(username, request);
    verify(mapper, never()).toProfileResponse(any(Trainer.class));
  }

  @Test
  @DisplayName("<updateTrainer> should update and return updated trainer with <200 OK>")
  void test_updateTrainer_shouldUpdateAndReturn_with200StatusCode() throws Exception {
    String username = "John.Smith";
    TrainerUpdateRequest request = new TrainerUpdateRequest("Ada", "Wong", true, 1L);
    Trainer trainer = new Trainer();
    TrainerProfileResponse.Specialization specialization =
        new TrainerProfileResponse.Specialization(1L, "Cardio");
    TrainerProfileResponse profileResp =
        new TrainerProfileResponse("John", "Doe", true, specialization, List.of());

    when(updateTrainerProfile.execute(anyString(), any(TrainerUpdateRequest.class)))
        .thenReturn(trainer);
    when(mapper.toProfileResponse(any(Trainer.class))).thenReturn(profileResp);

    mockMvc
        .perform(
            put("/trainers/{username}", username)
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jackson.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName", is(profileResp.firstName())))
        .andExpect(jsonPath("$.lastName", is(profileResp.lastName())))
        .andExpect(jsonPath("$.specialization.id", is(specialization.id().intValue())))
        .andExpect(jsonPath("$.specialization.name", is(specialization.name())))
        .andExpect(jsonPath("$.trainees", hasSize(0)));

    verify(updateTrainerProfile).execute(username, request);
    verify(mapper).toProfileResponse(trainer);
  }

  @Test
  @DisplayName("<changeTrainerProfileStatus> should return <401 UNAUTHORIZED> when no JWT provided")
  void test_changeTrainerProfileStatus_shouldReturn401StatusCode_whenNoJwtProvided()
      throws Exception {
    String username = "Rodia";
    boolean isActive = true;

    mockMvc
        .perform(
            patch("/trainers/{username}", username).param("isActive", Boolean.toString(isActive)))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.toString()))
        .andExpect(
            jsonPath("$.message").value("Full authentication is required to access this resource"));

    verify(updateTrainerProfileStatus, never()).execute(anyString(), anyBoolean());
  }

  @Test
  @DisplayName(
      "<changeTrainerProfileStatus> should update trainer profile status with <204 NO_CONTENT>")
  void test_changeTrainerProfileStatus_shouldUpdateTraineeStatus_with204StatusCode()
      throws Exception {
    String username = "Rodia";
    boolean isActive = true;

    doNothing().when(updateTrainerProfileStatus).execute(anyString(), anyBoolean());

    mockMvc
        .perform(
            patch("/trainers/{username}", username)
                .param("isActive", Boolean.toString(isActive))
                .with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isNoContent());

    verify(updateTrainerProfileStatus).execute(username, isActive);
  }

  @Test
  @DisplayName("<getTrainerTrainings> should return <401 UNAUTHORIZED> when no JWT provided")
  void test_getTrainerTrainings_shouldReturn401StatusCode_whenNoJwtProvided() throws Exception {
    String username = "John.Smith";
    LocalDate from = LocalDate.of(2025, 7, 11);
    LocalDate to = LocalDate.of(2025, 7, 12);
    String traineeName = "Kevin";

    mockMvc
        .perform(
            get("/trainers/{username}/trainings", username)
                .param("from", from.toString())
                .param("to", to.toString())
                .param("traineeNameLike", traineeName))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.toString()))
        .andExpect(
            jsonPath("$.message").value("Full authentication is required to access this resource"));

    verify(getTrainerTrainings, never())
        .execute(anyString(), any(LocalDate.class), any(LocalDate.class), anyString());
  }

  @Test
  @DisplayName("<getTrainerTrainings> should return trainer trainings with <200 OK>")
  void test_getTrainerTrainings_shouldReturnTrainings_with200StatusCode() throws Exception {
    String username = "John.Smith";
    LocalDate from = LocalDate.of(2025, 7, 11);
    LocalDate to = LocalDate.of(2025, 7, 12);
    LocalDate trainingDate = LocalDate.of(2025, 7, 12);
    String traineeName = "Kevin";

    List<TrainerTrainingResponse> expected =
        List.of(
            new TrainerTrainingResponse(
                "TR-1", trainingDate, "00:45:00", "Strength", "Kevin Kaslana"),
            new TrainerTrainingResponse(
                "TR-2", trainingDate, "00:30:00", "Stretch", "Kevin Murata"));

    when(getTrainerTrainings.execute(
            anyString(), any(LocalDate.class), any(LocalDate.class), anyString()))
        .thenReturn(expected);

    mockMvc
        .perform(
            get("/trainers/{username}/trainings", username)
                .param("from", from.toString())
                .param("to", to.toString())
                .param("traineeNameLike", traineeName)
                .with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$.[*].name", containsInAnyOrder("TR-1", "TR-2")))
        .andExpect(jsonPath("$[0].traineeName", is("Kevin Kaslana")));

    verify(getTrainerTrainings).execute(username, from, to, traineeName);
  }
}
