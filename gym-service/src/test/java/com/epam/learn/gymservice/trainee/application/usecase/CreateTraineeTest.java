package com.epam.learn.gymservice.trainee.application.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.learn.gymservice.trainee.adapter.api.rest.dto.TraineeCreateRequest;
import com.epam.learn.gymservice.trainee.adapter.spi.persistence.TraineeMapper;
import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.trainee.domain.repository.TraineeRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class CreateTraineeTest {

  @Mock private TraineeMapper mapper;
  @Mock private TraineeRepository repository;
  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private CreateTrainee createTraineeUseCase;

  @Test
  void test_execute_shouldPersistTraineeEntity() {
    TraineeCreateRequest request =
        new TraineeCreateRequest("John", "Doe", "password", LocalDate.now(), "Address");
    Trainee trainee =
        new Trainee(
            null,
            request.birthDate(),
            request.address(),
            request.firstName(),
            request.lastName(),
            "%s.%s".formatted(request.firstName(), request.lastName()),
            null,
            false,
            List.of(),
            Set.of());

    when(mapper.toEntity(any(TraineeCreateRequest.class))).thenReturn(trainee);
    when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    when(repository.save(any(Trainee.class))).thenReturn(trainee);

    Trainee result = assertDoesNotThrow(() -> createTraineeUseCase.execute(request));

    assertNotNull(result);
    assertEquals(request.firstName(), result.getUser().getFirstName());
    assertEquals(request.lastName(), result.getUser().getLastName());
    assertEquals("John.Doe", result.getUser().getUsername());
    assertEquals("encodedPassword", result.getUser().getPassword());

    verify(mapper).toEntity(request);
    verify(passwordEncoder).encode(request.password());
    verify(repository).save(trainee);
  }
}
