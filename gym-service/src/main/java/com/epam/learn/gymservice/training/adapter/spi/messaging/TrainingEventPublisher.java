package com.epam.learn.gymservice.training.adapter.spi.messaging;

import com.epam.learn.gymservice.training.adapter.api.rest.dto.TrainingSessionEvent;
import lombok.AllArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TrainingEventPublisher {

  public static final String EVENT_DESTINATION = "training.session.queue";

  private final JmsTemplate jmsTemplate;

  public void publishEvent(TrainingSessionEvent event) {
    jmsTemplate.convertAndSend(EVENT_DESTINATION, event);
  }
}
