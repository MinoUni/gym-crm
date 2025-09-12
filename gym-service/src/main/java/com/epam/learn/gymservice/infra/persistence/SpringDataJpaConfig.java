package com.epam.learn.gymservice.infra.persistence;

import io.hypersistence.utils.spring.repository.BaseJpaRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
    value = "com.epam.learn.gymservice",
    repositoryBaseClass = BaseJpaRepositoryImpl.class)
public class SpringDataJpaConfig {}
