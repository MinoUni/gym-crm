package com.epam.learn.trsessionservice.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;
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
  private Boolean isActive;
  private LocalDate trainingDate;
  private LocalTime trainingDuration;
}
