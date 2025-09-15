package com.epam.learn.trsessionservice.domain.service;

import com.epam.learn.trsessionservice.adapter.api.rest.dto.TrainerTrainingSessionsReport;

public interface TrainingSessionReportBuilder {

  TrainerTrainingSessionsReport build(String trainerUsername);
}
