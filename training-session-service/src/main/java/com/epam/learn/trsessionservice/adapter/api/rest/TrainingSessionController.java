package com.epam.learn.trsessionservice.adapter.api.rest;

import com.epam.learn.trsessionservice.adapter.api.rest.dto.TrainerTrainingSessionsReport;
import com.epam.learn.trsessionservice.adapter.api.rest.dto.TrainingSessionEvent;
import com.epam.learn.trsessionservice.domain.service.TrainingSessionHandler;
import com.epam.learn.trsessionservice.domain.service.TrainingSessionReportBuilder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/training-sessions")
public class TrainingSessionController {

  private final TrainingSessionHandler trainingSessionHandler;
  private final TrainingSessionReportBuilder trainingSessionReportBuilder;

  @PostMapping
  public ResponseEntity<String> createTrainingSessionEvent(
      @Valid @RequestBody TrainingSessionEvent event) {
    trainingSessionHandler.handle(event);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{trainerUsername}")
  public ResponseEntity<TrainerTrainingSessionsReport> findTrainerWorkingHoursInfo(
      @PathVariable String trainerUsername) {
    return ResponseEntity.ok().body(trainingSessionReportBuilder.build(trainerUsername));
  }
}
