package com.epam.learn.gymservice.trainee.adapter.api.rest;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.learn.gymservice.infra.exception.EntityNotFoundException;
import com.epam.learn.gymservice.infra.security.WebSecurityConfig;
import com.epam.learn.gymservice.infra.security.jwt.JwtProvider;
import com.epam.learn.gymservice.infra.security.web.JwtAuthenticationEntryPoint;
import com.epam.learn.gymservice.trainee.adapter.api.rest.dto.TraineeCreateRequest;
import com.epam.learn.gymservice.trainee.adapter.api.rest.dto.TraineeProfileResponse;
import com.epam.learn.gymservice.trainee.adapter.api.rest.dto.TraineeTrainingResponse;
import com.epam.learn.gymservice.trainee.adapter.api.rest.dto.TraineeUpdateRequest;
import com.epam.learn.gymservice.trainee.adapter.spi.persistence.TraineeMapper;
import com.epam.learn.gymservice.trainee.application.usecase.CreateTrainee;
import com.epam.learn.gymservice.trainee.application.usecase.DeleteTraineeProfile;
import com.epam.learn.gymservice.trainee.application.usecase.GetAvailableTrainersForTrainee;
import com.epam.learn.gymservice.trainee.application.usecase.GetTraineeProfile;
import com.epam.learn.gymservice.trainee.application.usecase.GetTraineeTrainings;
import com.epam.learn.gymservice.trainee.application.usecase.UpdateTraineeProfile;
import com.epam.learn.gymservice.trainee.application.usecase.UpdateTraineeTrainers;
import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.training.domain.model.TrainingType;
import com.epam.learn.gymservice.user.application.usecase.UserProfileStatusUpdateUseCase;
import com.epam.learn.gymservice.user.domain.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
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

@WebMvcTest(TraineeController.class)
@Import({WebSecurityConfig.class, JwtProvider.class, JwtAuthenticationEntryPoint.class})
class TraineeControllerTest {

  @MockitoBean private TraineeMapper mapper;
  @MockitoBean private CreateTrainee createTrainee;
  @MockitoBean private GetTraineeProfile getTraineeProfile;
  @MockitoBean private GetTraineeTrainings getTraineeTrainings;
  @MockitoBean private GetAvailableTrainersForTrainee getAvailableTrainersForTrainee;
  @MockitoBean private UpdateTraineeProfile updateTraineeProfile;
  @MockitoBean private UserProfileStatusUpdateUseCase updateTraineeProfileStatus;
  @MockitoBean private UpdateTraineeTrainers updateTraineeTrainers;
  @MockitoBean private DeleteTraineeProfile deleteTraineeProfile;

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper jackson;

  @Test
  @DisplayName("<createTrainee> should create trainee and return credentials with <201 CREATED>")
  void test_createTrainee_shouldCreateTrainee_with201StatusCode() throws Exception {
    TraineeCreateRequest request =
        new TraineeCreateRequest(
            "John", "Doe", "password", LocalDate.now().minusDays(3), "Address");
    Trainee trainee = new Trainee();
    trainee.setUser(
        new User(
            null, request.firstName(), request.lastName(), "John.Doe", request.password(), false));

    when(createTrainee.execute(any(TraineeCreateRequest.class))).thenReturn(trainee);

    mockMvc
        .perform(
            post("/trainees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jackson.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.username").value("John.Doe"))
        .andExpect(jsonPath("$.password").exists());

    verify(createTrainee).execute(request);
  }

  @Test
  @DisplayName("<createTrainee> should fail validation with <400 BAD_REQUEST>")
  void test_createTrainee_shouldFailValidation_with400StatusCode() throws Exception {
    TraineeCreateRequest request =
        new TraineeCreateRequest(null, "  ", "password", LocalDate.now(), null);

    mockMvc
        .perform(
            post("/trainees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jackson.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
        .andExpect(
            jsonPath("$.message")
                .value("Validation failed for obj='traineeCreateRequest'. Error count: 3"))
        .andExpect(jsonPath("$.fieldErrors", hasSize(3)))
        .andExpect(
            jsonPath(
                "$.fieldErrors[*].property",
                containsInAnyOrder("firstName", "lastName", "birthDate")));

    verify(createTrainee, never()).execute(request);
  }

  @Test
  @DisplayName("<getTraineeProfile> should get trainee profile info with <200 OK>")
  void test_getTraineeProfile_shouldReturnTraineeProfile_with200StatusCode() throws Exception {
    String username = "John.Doe";
    Trainee trainee = new Trainee();
    TraineeProfileResponse profileResp =
        new TraineeProfileResponse(
            username, "John", "Doe", LocalDate.now(), "Adress", true, List.of());

    when(getTraineeProfile.execute(anyString())).thenReturn(trainee);
    when(mapper.toProfileResponse(any(Trainee.class))).thenReturn(profileResp);

    mockMvc
        .perform(
            get("/trainees/{username}", username).with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.username").value(profileResp.username()))
        .andExpect(jsonPath("$.firstName").value(profileResp.firstName()))
        .andExpect(jsonPath("$.lastName").value(profileResp.lastName()));

    verify(getTraineeProfile).execute(username);
    verify(mapper).toProfileResponse(trainee);
  }

  @Test
  @DisplayName("<getTraineeProfile> should return <401 UNAUTHORIZED> when no JWT provided")
  void test_getTraineeProfile_shouldReturn401StatusCode_whenNoJwtProvided() throws Exception {
    String username = "John.Doe";

    mockMvc
        .perform(get("/trainees/{username}", username))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.toString()))
        .andExpect(
            jsonPath("$.message").value("Full authentication is required to access this resource"));

    verify(getTraineeProfile, never()).execute(username);
    verify(mapper, never()).toProfileResponse(any(Trainee.class));
  }

  @Test
  @DisplayName("<getTraineeProfile> should throw <TraineeNotFoundException> with <404 NOT_FOUND>")
  void test_getTraineeProfile_shouldThrowTraineeNotFoundException_with404StatusCode()
      throws Exception {
    String username = "John.Doe";

    when(getTraineeProfile.execute(anyString()))
        .thenThrow(new EntityNotFoundException(Trainee.class, username));

    mockMvc
        .perform(
            get("/trainees/{username}", username).with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.name()))
        .andExpect(
            jsonPath("$.message")
                .value(
                    "Unable to find <%s> profile with username='%s'"
                        .formatted(Trainee.class.getSimpleName(), username)));

    verify(getTraineeProfile).execute(username);
    verify(mapper, never()).toProfileResponse(any(Trainee.class));
  }

  @Test
  @DisplayName("<deleteTrainee> should return <401 UNAUTHORIZED> when no JWT provided")
  void test_deleteTrainee_shouldReturn401StatusCode_whenNoJwtProvided() throws Exception {
    String username = "John.Smith";

    mockMvc
        .perform(delete("/trainees/{username}", username))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.toString()))
        .andExpect(
            jsonPath("$.message").value("Full authentication is required to access this resource"));

    verify(deleteTraineeProfile, never()).execute(username);
  }

  @Test
  @DisplayName("<deleteTrainee> should delete entity with <200 OK>")
  void test_deleteTrainee_shouldDeleteTraineeProfile_with200StatusCode() throws Exception {
    String username = "John.Smith";

    doNothing().when(deleteTraineeProfile).execute(anyString());

    mockMvc
        .perform(
            delete("/trainees/{username}", username)
                .with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk());

    verify(deleteTraineeProfile).execute(username);
  }

  @Test
  @DisplayName("<updateTrainee> should return <401 UNAUTHORIZED> when no JWT provided")
  void test_updateTrainee_shouldReturn401StatusCode_whenNoJwtProvided() throws Exception {
    String username = "John.Smith";
    TraineeUpdateRequest request = new TraineeUpdateRequest("Jorge", "Larson", null, null, true);

    mockMvc
        .perform(
            put("/trainees/{username}", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jackson.writeValueAsString(request)))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.toString()))
        .andExpect(
            jsonPath("$.message").value("Full authentication is required to access this resource"));

    verify(updateTraineeProfile, never()).execute(username, request);
    verify(mapper, never()).toProfileResponse(any(Trainee.class));
  }

  @Test
  @DisplayName("<updateTrainee> should update and return updated trainee profile with <200 OK>")
  void test_updateTrainee_shouldUpdateAndReturn_with200StatusCode() throws Exception {
    String username = "John.Smith";
    TraineeUpdateRequest request = new TraineeUpdateRequest("Jorge", "Larson", null, null, true);
    Trainee trainee = new Trainee();
    TraineeProfileResponse profileResp =
        new TraineeProfileResponse(
            username,
            request.firstName(),
            request.lastName(),
            LocalDate.now(),
            "Address",
            request.isActive(),
            List.of());

    when(updateTraineeProfile.execute(anyString(), any(TraineeUpdateRequest.class)))
        .thenReturn(trainee);
    when(mapper.toProfileResponse(any(Trainee.class))).thenReturn(profileResp);

    mockMvc
        .perform(
            put("/trainees/{username}", username)
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jackson.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.username").value(profileResp.username()))
        .andExpect(jsonPath("$.firstName").value(profileResp.firstName()))
        .andExpect(jsonPath("$.lastName").value(profileResp.lastName()));

    verify(updateTraineeProfile).execute(username, request);
    verify(mapper).toProfileResponse(trainee);
  }

  @Test
  @DisplayName("<updateTrainee> should fail validation with <400 BAD_REQUEST>")
  void test_updateTrainee_shouldFailValidation_with400StatusCode() throws Exception {
    String username = "John.Smith";
    TraineeUpdateRequest request = new TraineeUpdateRequest(null, " ", LocalDate.now(), null, null);

    mockMvc
        .perform(
            put("/trainees/{username}", username)
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jackson.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
        .andExpect(
            jsonPath("$.message")
                .value("Validation failed for obj='traineeUpdateRequest'. Error count: 4"))
        .andExpect(jsonPath("$.fieldErrors", hasSize(4)))
        .andExpect(
            jsonPath(
                "$.fieldErrors[*].property",
                containsInAnyOrder("firstName", "lastName", "birthDate", "isActive")));

    verify(updateTraineeProfile, never()).execute(username, request);
    verify(mapper, never()).toProfileResponse(any(Trainee.class));
  }

  @Test
  @DisplayName("<getTraineeTrainings> should return <401 UNAUTHORIZED> when no JWT provided")
  void test_getTraineeTrainings_shouldReturn401StatusCode_whenNoJwtProvided() throws Exception {
    String username = "John.Smith";
    LocalDate from = LocalDate.of(2025, 7, 11);
    LocalDate to = LocalDate.of(2025, 7, 12);
    String trainerName = "Kevin";
    String trainingType = "Str";

    mockMvc
        .perform(
            get("/trainees/{username}/trainings", username)
                .param("from", from.toString())
                .param("to", to.toString())
                .param("trainerName", trainerName)
                .param("trainingType", trainingType))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.toString()))
        .andExpect(
            jsonPath("$.message").value("Full authentication is required to access this resource"));

    verify(getTraineeTrainings, never())
        .execute(anyString(), any(), any(), anyString(), anyString());
  }

  @Test
  @DisplayName("<getTraineeTrainings> should return trainee trainings with <200 OK>")
  void test_getTraineeTrainings_shouldReturnTrainings_with200StatusCode() throws Exception {
    String username = "John.Smith";
    LocalDate from = LocalDate.of(2025, 7, 11);
    LocalDate to = LocalDate.of(2025, 7, 12);
    LocalDate trainingDate = LocalDate.of(2025, 7, 12);
    String trainerName = "Kevin";
    String trainingType = "Str";

    List<TraineeTrainingResponse> expected =
        List.of(
            new TraineeTrainingResponse(
                "TR-1", trainingDate, "00:45:00", "Strength", "Kevin Kaslana"),
            new TraineeTrainingResponse(
                "TR-2", trainingDate, "00:30:00", "Stretch", "Kevin Murata"));

    when(getTraineeTrainings.execute(
            anyString(), any(LocalDate.class), any(LocalDate.class), anyString(), anyString()))
        .thenReturn(expected);

    mockMvc
        .perform(
            get("/trainees/{username}/trainings", username)
                .param("from", from.toString())
                .param("to", to.toString())
                .param("trainerName", trainerName)
                .param("trainingType", trainingType)
                .with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$.[*].name", containsInAnyOrder("TR-1", "TR-2")))
        .andExpect(jsonPath("$[0].trainerName", is("Kevin Kaslana")));

    verify(getTraineeTrainings).execute(username, from, to, trainerName, trainingType);
  }

  @Test
  @DisplayName("<changeTraineeProfileStatus> should return <401 UNAUTHORIZED> when no JWT provided")
  void test_changeTraineeProfileStatus_shouldReturn401StatusCode_whenNoJwtProvided()
      throws Exception {
    String username = "Rodia";
    boolean isActive = true;

    mockMvc
        .perform(
            patch("/trainees/{username}", username).param("isActive", Boolean.toString(isActive)))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.toString()))
        .andExpect(
            jsonPath("$.message").value("Full authentication is required to access this resource"));

    verify(updateTraineeProfileStatus, never()).execute(anyString(), anyBoolean());
  }

  @Test
  @DisplayName("<changeTraineeProfileStatus> should update trainee profile status with <200 OK>")
  void test_changeTraineeProfileStatus_shouldUpdateTraineeStatus_with200StatusCode()
      throws Exception {
    String username = "Rodia";
    boolean isActive = true;

    doNothing().when(updateTraineeProfileStatus).execute(anyString(), anyBoolean());

    mockMvc
        .perform(
            patch("/trainees/{username}", username)
                .param("isActive", Boolean.toString(isActive))
                .with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk());

    verify(updateTraineeProfileStatus).execute(username, isActive);
  }

  @Test
  @DisplayName("<getAvailableTrainers> should return <401 UNAUTHORIZED> when no JWT provided")
  void test_getAvailableTrainers_shouldReturn401StatusCode_whenNoJwtProvided() throws Exception {
    String username = "Castorice";

    mockMvc
        .perform(get("/trainees/{username}/trainers/available", username))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.toString()))
        .andExpect(
            jsonPath("$.message").value("Full authentication is required to access this resource"));

    verify(getAvailableTrainersForTrainee, never()).execute(anyString());
    verify(mapper, never()).toTrainerProfile(any(Trainer.class));
  }

  @Test
  @DisplayName("<getAvailableTrainers> should return list of available trainers with <200 OK>")
  void test_getAvailableTrainers_shouldReturnAvailableTrainers_with200StatusCode()
      throws Exception {
    String username = "Castorice";
    User user = new User(1L, "Castorice", "Hades", username, "pass", true);
    TrainingType trainingType = new TrainingType(1L, "Strength", List.of("1", "2", "3"));
    Trainer trainer = new Trainer(user, trainingType);
    var trainerProfile =
        new TraineeProfileResponse.TrainerProfile(
            username, user.getFirstName(), user.getLastName(), trainingType.getName());

    when(getAvailableTrainersForTrainee.execute(anyString())).thenReturn(List.of(trainer));
    when(mapper.toTrainerProfile(any(Trainer.class))).thenReturn(trainerProfile);

    mockMvc
        .perform(
            get("/trainees/{username}/trainers/available", username)
                .with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].username", is(trainerProfile.username())))
        .andExpect(jsonPath("$[0].firstName", is(trainerProfile.firstName())))
        .andExpect(jsonPath("$[0].lastName", is(trainerProfile.lastName())))
        .andExpect(jsonPath("$[0].specialization", is(trainerProfile.specialization())));

    verify(getAvailableTrainersForTrainee).execute(username);
    verify(mapper).toTrainerProfile(trainer);
  }

  @Test
  @DisplayName("<updateTraineeTrainers> should return <401 UNAUTHORIZED> when no JWT provided")
  void test_updateTraineeTrainers_shouldReturn401StatusCode_whenNoJwtProvided() throws Exception {
    String username = "Bob";
    List<String> trainerUsernames = List.of("Bob", "Sam");

    mockMvc
        .perform(
            put("/trainees/{username}/trainers", username)
                .param("trainerUsernames", String.join(",", trainerUsernames)))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.toString()))
        .andExpect(
            jsonPath("$.message").value("Full authentication is required to access this resource"));

    verify(updateTraineeTrainers, never()).execute(anyString(), anyList());
    verify(mapper, never()).toTrainerProfile(any(Trainer.class));
  }

  @Test
  @DisplayName("<updateTraineeTrainers> should return updated trainee trainer list with <200 OK>")
  void test_updateTraineeTrainers_shouldUpdateTraineeTrainerListWith200() throws Exception {
    String username = "Bob";
    List<String> trainerUsernames = List.of("Bob", "Sam");
    User user = new User(1L, "Castorice", "Hades", username, "pass", true);
    TrainingType trainingType = new TrainingType(1L, "Strength", List.of("1", "2", "3"));
    Trainer trainer = new Trainer(user, trainingType);
    var trainerProfile =
        new TraineeProfileResponse.TrainerProfile(
            username, user.getFirstName(), user.getLastName(), trainingType.getName());

    when(updateTraineeTrainers.execute(anyString(), anyList())).thenReturn(Set.of(trainer));
    when(mapper.toTrainerProfile(any(Trainer.class))).thenReturn(trainerProfile);

    mockMvc
        .perform(
            put("/trainees/{username}/trainers", username)
                .param("trainerUsernames", String.join(",", trainerUsernames))
                .with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].username", is(trainerProfile.username())))
        .andExpect(jsonPath("$[0].firstName", is(trainerProfile.firstName())))
        .andExpect(jsonPath("$[0].lastName", is(trainerProfile.lastName())))
        .andExpect(jsonPath("$[0].specialization", is(trainerProfile.specialization())));

    verify(updateTraineeTrainers).execute(username, trainerUsernames);
    verify(mapper).toTrainerProfile(trainer);
  }
}
