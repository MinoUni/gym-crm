package com.epam.learn.gymservice.infra.config;

import com.epam.learn.gymservice.Main;
import com.epam.learn.gymservice.infra.stereotype.DomainService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
    basePackageClasses = {Main.class},
    includeFilters = {
      @ComponentScan.Filter(
          type = FilterType.ANNOTATION,
          classes = {DomainService.class})
    })
public class DomainConfig {}
