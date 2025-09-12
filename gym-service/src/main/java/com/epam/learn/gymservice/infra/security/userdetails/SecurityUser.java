package com.epam.learn.gymservice.infra.security.userdetails;

import com.epam.learn.gymservice.user.domain.model.User;
import java.util.Collection;
import java.util.Objects;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

public class SecurityUser extends org.springframework.security.core.userdetails.User {

  @Getter private final transient User user;

  public SecurityUser(User user, Collection<? extends GrantedAuthority> authorities) {
    super(user.getUsername(), user.getPassword(), authorities);
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof SecurityUser that)) return false;
    if (!super.equals(o)) return false;
    return Objects.equals(user, that.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), user);
  }
}
