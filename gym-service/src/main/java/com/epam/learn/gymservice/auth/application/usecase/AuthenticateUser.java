package com.epam.learn.gymservice.auth.application.usecase;

import com.epam.learn.gymservice.auth.adapter.api.rest.dto.AuthCredentials;
import com.epam.learn.gymservice.infra.security.jwt.JwtProvider;
import com.epam.learn.gymservice.infra.security.userdetails.SecurityUser;
import com.epam.learn.gymservice.infra.stereotype.DomainService;
import com.epam.learn.gymservice.user.domain.model.User;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@DomainService
@AllArgsConstructor
public class AuthenticateUser {

  private final JwtProvider jwtProvider;
  private final AuthenticationManager authenticationManager;

  public String execute(AuthCredentials credentials) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                credentials.username(), credentials.password()));
    return jwtProvider.generateAccessToken(buildClaims(authentication));
  }

  private Map<String, Object> buildClaims(Authentication authentication) {
    User user = ((SecurityUser) authentication.getPrincipal()).getUser();
    List<String> authorities =
        authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    return Map.of(
        "username", user.getUsername(),
        "firstName", user.getFirstName(),
        "lastName", user.getLastName(),
        "roles", authorities);
  }
}
