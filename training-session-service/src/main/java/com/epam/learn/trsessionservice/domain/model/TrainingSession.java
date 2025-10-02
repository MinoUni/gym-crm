package com.epam.learn.trsessionservice.domain.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSession {

  private Long id;
  private String trainerUsername;
  private String trainerFirstName;
  private String trainerLastName;
  private Boolean trainerStatus;

  @Builder.Default private List<TrainingSessionYear> years = new ArrayList<>();

  public void addYear(TrainingSessionYear year) {
    if (years == null) {
      return;
    }
    if (years.contains(year)) {
      years.stream()
          .filter(y -> y.equals(year))
          .findFirst()
          .ifPresent(y -> year.getMonths().forEach(y::addMonth));
      return;
    }
    years.add(year);
  }
}
