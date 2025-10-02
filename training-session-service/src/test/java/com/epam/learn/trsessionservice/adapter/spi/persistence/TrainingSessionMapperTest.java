package com.epam.learn.trsessionservice.adapter.spi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.learn.trsessionservice.adapter.api.rest.dto.TrainingSessionEvent;
import com.epam.learn.trsessionservice.domain.model.ActionType;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class TrainingSessionMapperTest {

  private final TrainingSessionMapper mapper = new TrainingSessionMapperImpl();

  @Test
  void shouldMapEventToDomain() {
    // Given
    var event =
        new TrainingSessionEvent(
            1L,
            "John.Doe",
            "John",
            "Doe",
            true,
            LocalDate.of(2025, 12, 10),
            LocalTime.of(1, 15),
            ActionType.ADD);

    // When
    var domain = mapper.toDomain(event);

    // Then
    assertNotNull(domain);
    assertEquals(event.id(), domain.getId());
    assertEquals("John.Doe", domain.getTrainerUsername());
    assertEquals("John", domain.getTrainerFirstName());
    assertEquals("Doe", domain.getTrainerLastName());
    assertTrue(domain.getTrainerStatus());

    var yearWorkload = domain.getYears().getFirst();

    assertEquals(event.trainingDate().getYear(), yearWorkload.getYear());
    assertEquals(
        event.trainingDate().getMonth().name(), yearWorkload.getMonths().getFirst().getMonth());
    assertEquals(
        event.trainingDuration().toSecondOfDay(),
        yearWorkload.getMonths().getFirst().getWorkload());
  }
}
