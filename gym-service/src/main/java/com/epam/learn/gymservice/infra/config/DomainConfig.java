package com.epam.learn.gymservice.infra.config;

import com.epam.learn.gymservice.GymServiceApplication;
import com.epam.learn.gymservice.infra.stereotype.DomainService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
    basePackageClasses = {GymServiceApplication.class},
    includeFilters = {
      @ComponentScan.Filter(
          type = FilterType.ANNOTATION,
          classes = {DomainService.class})
    })
public class DomainConfig {}
