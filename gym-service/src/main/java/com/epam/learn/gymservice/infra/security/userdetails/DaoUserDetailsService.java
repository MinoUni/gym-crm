package com.epam.learn.gymservice.infra.security.userdetails;

import com.epam.learn.gymservice.user.domain.repository.UserRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DaoUserDetailsService implements UserDetailsService {

  public static final String ROLE_USER = "USER";

  private final UserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository
        .findByUsername(username)
        .map(u -> new SecurityUser(u, List.of(() -> ROLE_USER)))
        .orElseThrow(
            () ->
                new UsernameNotFoundException(
                    "User with username <%s> not found!".formatted(username)));
  }
}
