package com.epam.learn.gymservice.bdd.config;

import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.trainee.domain.repository.TraineeRepository;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.trainer.domain.repository.TrainerRepository;
import com.epam.learn.gymservice.training.domain.model.TrainingType;
import com.epam.learn.gymservice.training.domain.repository.TrainingTypeRepository;
import com.epam.learn.gymservice.user.domain.model.User;
import io.cucumber.spring.CucumberContextConfiguration;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberSpringConfig {

  @TestConfiguration
  @EnableWebSecurity
  static class TestWebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      return http.csrf(AbstractHttpConfigurer::disable)
          .sessionManagement(
              session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
          .authorizeHttpRequests(req -> req.anyRequest().permitAll())
          .build();
    }
  }

  @TestConfiguration
  static class TestDataInitializer {

    @Bean
    CommandLineRunner loadTestData(
        TrainerRepository trainerRepository,
        TraineeRepository traineeRepository,
        TrainingTypeRepository trainingTypeRepository) {
      return args -> {
        var spec = trainingTypeRepository.save(new TrainingType(null, "Yoga", List.of()));
        trainerRepository.save(
            new Trainer(new User(null, "Kevin", "Kaslana", "Kevin.Kaslana", true), spec));
        traineeRepository.save(
            new Trainee(null, null, null, "Aki", "Shroom", "Aki.Shroom", true, null, null));
      };
    }
  }
}
