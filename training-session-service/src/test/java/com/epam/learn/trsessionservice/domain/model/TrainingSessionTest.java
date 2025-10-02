package com.epam.learn.trsessionservice.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Month;
import org.junit.jupiter.api.Test;

class TrainingSessionTest {

  @Test
  void shouldAddYearIfNotExists() {
    TrainingSession session = new TrainingSession();

    TrainingSessionYear year2023 = new TrainingSessionYear(2023);
    session.addYear(year2023);

    assertEquals(1, session.getYears().size());
    assertTrue(session.getYears().contains(year2023));
  }

  @Test
  void shouldMergeMonthsIfYearExists() {
    var session = new TrainingSession();
    var year2023 = new TrainingSessionYear(2023);
    year2023.addMonth(new TrainingSessionMonth(Month.JANUARY.toString(), 3600));
    session.addYear(year2023);

    var anotherYear2023 = new TrainingSessionYear(2023);
    year2023.addMonth(new TrainingSessionMonth(Month.FEBRUARY.toString(), 3600));
    session.addYear(anotherYear2023);

    assertEquals(1, session.getYears().size());
    assertEquals(2, session.getYears().getFirst().getMonths().size());
  }
}
