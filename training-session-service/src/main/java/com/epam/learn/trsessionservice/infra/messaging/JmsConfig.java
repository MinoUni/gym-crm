package com.epam.learn.trsessionservice.infra.messaging;

import com.epam.learn.trsessionservice.adapter.api.rest.dto.TrainingSessionEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.transaction.PlatformTransactionManager;

@EnableJms
@Configuration
public class JmsConfig {

  @Bean
  public ActiveMQConnectionFactory connectionFactory(
      @Value("${spring.artemis.broker-url}") String brokerUrl,
      @Value("${spring.artemis.user}") String user,
      @Value("${spring.artemis.password}") String password) {
    return new ActiveMQConnectionFactory(brokerUrl, user, password);
  }

  @Bean
  public JmsTemplate jmsTemplate(
      ActiveMQConnectionFactory connectionFactory, MessageConverter messageConverter) {
    JmsTemplate template = new JmsTemplate(connectionFactory);
    template.setMessageConverter(messageConverter);
    template.setDeliveryPersistent(true);
    template.setSessionTransacted(true);
    return template;
  }

  @Bean
  public DefaultJmsListenerContainerFactory myFactory(
      ActiveMQConnectionFactory connectionFactory,
      DefaultJmsListenerContainerFactoryConfigurer configurer,
      MessageConverter messageConverter,
      PlatformTransactionManager transactionManager) {
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    configurer.configure(factory, connectionFactory);
    factory.setMessageConverter(messageConverter);
    factory.setTransactionManager(transactionManager);
    return factory;
  }

  @Bean
  public PlatformTransactionManager transactionManager(ActiveMQConnectionFactory connectionFactory) {
      return new JmsTransactionManager(connectionFactory);
  }

  @Bean
  public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setObjectMapper(objectMapper);
    converter.setTypeIdPropertyName("_type");
    converter.setTypeIdMappings(Map.of("TrainingSessionEvent", TrainingSessionEvent.class));
    return converter;
  }
}
