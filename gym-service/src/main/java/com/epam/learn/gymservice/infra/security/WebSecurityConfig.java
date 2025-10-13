package com.epam.learn.gymservice.infra.security;

import static org.springframework.http.HttpMethod.POST;

import com.epam.learn.gymservice.infra.security.converter.KeyCloakRoleConverter;
import com.epam.learn.gymservice.infra.security.web.JwtAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Profile("!test")
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      JwtAuthenticationEntryPoint authEntryPoint,
      KeyCloakRoleConverter converter)
      throws Exception {
    return http.formLogin(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .oauth2ResourceServer(
            oauth2 ->
                oauth2
                    .authenticationEntryPoint(authEntryPoint)
                    .jwt(jwt -> jwt.jwtAuthenticationConverter(converter)))
        .authorizeHttpRequests(
            req ->
                req.requestMatchers(POST, "/trainees", "/trainers")
                    .permitAll()
                    .anyRequest()
                    .hasRole("USER"))
        .build();
  }
}
