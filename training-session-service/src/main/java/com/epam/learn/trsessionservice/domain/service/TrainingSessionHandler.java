package com.epam.learn.trsessionservice.domain.service;

import com.epam.learn.trsessionservice.adapter.api.rest.dto.TrainingSessionEvent;

public interface TrainingSessionHandler {

  void handle(TrainingSessionEvent event);
}
