package com.epam.learn.gymservice.training.adapter.api.rest;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.learn.gymservice.infra.security.WebSecurityConfig;
import com.epam.learn.gymservice.infra.security.jwt.JwtProvider;
import com.epam.learn.gymservice.infra.security.web.JwtAuthenticationEntryPoint;
import com.epam.learn.gymservice.training.adapter.api.rest.dto.TrainingCreateRequest;
import com.epam.learn.gymservice.training.adapter.api.rest.dto.TrainingTypeResponse;
import com.epam.learn.gymservice.training.application.usecase.CreateTrainingUseCase;
import com.epam.learn.gymservice.training.application.usecase.GetTrainingTypesUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TrainingController.class)
@Import({WebSecurityConfig.class, JwtProvider.class, JwtAuthenticationEntryPoint.class})
class TrainingControllerTest {

  @Autowired private ObjectMapper jackson;
  @Autowired private MockMvc mockMvc;

  @MockitoBean private CreateTrainingUseCase createTraining;
  @MockitoBean private GetTrainingTypesUseCase getTrainingTypes;

  @Test
  @DisplayName("<getTrainingTypes> should return list of training types with <200 OK>")
  void test_getTrainingTypes_shouldReturnTrainingTypeList_with200StatusCode() throws Exception {
    TrainingTypeResponse type = new TrainingTypeResponse(1L, "TR");
    List<TrainingTypeResponse> typeList = List.of(type);

    when(getTrainingTypes.execute()).thenReturn(typeList);

    mockMvc
        .perform(get("/trainings/types").with(SecurityMockMvcRequestPostProcessors.jwt()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(typeList.size())))
        .andExpect(jsonPath("$[0].name", is(type.name())))
        .andExpect(jsonPath("$[0].id", is(type.id().intValue())));

    verify(getTrainingTypes).execute();
  }

  @Test
  @DisplayName("<getTrainingTypes> should return <401 UNAUTHORIZED> when no JWT provided")
  void test_getTrainingTypes_shouldReturn401StatusCode_whenNoJwtProvided() throws Exception {
    mockMvc.perform(get("/trainings/types")).andExpect(status().isUnauthorized());

    verify(getTrainingTypes, never()).execute();
  }

  @Test
  @DisplayName("<createTraining> should return <401 UNAUTHORIZED> when no JWT provided")
  void test_createTraining_shouldReturn401StatusCode_whenNoJwtProvided() throws Exception {
    TrainingCreateRequest request =
        new TrainingCreateRequest(
            "TR", LocalDate.now(), LocalTime.of(1, 30), 1L, "trainer", "trainee");

    mockMvc
        .perform(
            post("/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jackson.writeValueAsString(request)))
        .andExpect(status().isUnauthorized());

    verify(createTraining, never()).execute(request);
  }

  @Test
  @DisplayName("<createTraining> should fail validation with <400 BAD_REQUEST>")
  void test_createTraining_shouldFailValidation_with400StatusCode() throws Exception {
    TrainingCreateRequest request =
        new TrainingCreateRequest(null, null, null, 0L, "  ", "trainee");

    mockMvc
        .perform(
            post("/trainings")
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jackson.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
        .andExpect(
            jsonPath("$.message")
                .value("Validation failed for obj='trainingCreateRequest'. Error count: 5"))
        .andExpect(jsonPath("$.fieldErrors", hasSize(5)))
        .andExpect(
            jsonPath(
                "$.fieldErrors[*].property",
                containsInAnyOrder(
                    "name", "date", "duration", "trainingTypeId", "trainerUsername")));

    verify(createTraining, never()).execute(request);
  }

  @Test
  @DisplayName("<createTraining> should create training with <204 NO_CONTENT>")
  void test_createTraining_shouldCreateTraining_with200StatusCode() throws Exception {
    TrainingCreateRequest request =
        new TrainingCreateRequest(
            "TR", LocalDate.now(), LocalTime.of(1, 30), 1L, "trainer", "trainee");

    doNothing().when(createTraining).execute(any(TrainingCreateRequest.class));

    mockMvc
        .perform(
            post("/trainings")
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jackson.writeValueAsString(request)))
        .andExpect(status().isNoContent());

    verify(createTraining).execute(request);
  }
}
