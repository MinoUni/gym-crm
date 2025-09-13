package com.epam.learn.gymservice.training.adapter.spi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Duration;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class TrainingMapperTest {

  private final TrainingMapper mapper = new TrainingMapperImpl();

  @Test
  void test_formatDuration() {
    Duration duration = Duration.ofMinutes(90);

    String result = mapper.formatDuration(duration);

    assertNotNull(result);
    assertEquals("01:30:00", result);
  }

  @Test
  void test_toDuration() {
    LocalTime time = LocalTime.of(1, 30);

    Duration result = mapper.toDuration(time);

    assertNotNull(result);
    assertEquals(Duration.ofHours(1).plusMinutes(30), result);
  }
}
