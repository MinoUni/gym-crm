package com.epam.learn.gymservice.infra.security.converter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class KeyCloakRoleConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private static final String REALM_ACCESS = "realm_access";
  private static final String PREFERRED_USERNAME = "preferred_username";

  @Override
  public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
    Map<String, Object> realmRoles = jwt.getClaimAsMap(REALM_ACCESS);
    if (realmRoles != null && realmRoles.containsKey("roles")) {
      Collection<? extends GrantedAuthority> authorities =
          ((List<String>) realmRoles.get("roles"))
              .stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).toList();
      return new JwtAuthenticationToken(jwt, authorities, jwt.getClaimAsString(PREFERRED_USERNAME));
    }
    return new JwtAuthenticationToken(jwt, List.of(), jwt.getClaimAsString(PREFERRED_USERNAME));
  }
}
