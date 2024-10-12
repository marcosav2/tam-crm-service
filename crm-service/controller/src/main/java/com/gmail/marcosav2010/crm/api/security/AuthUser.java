package com.gmail.marcosav2010.crm.api.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gmail.marcosav2010.crm.user.entities.User;
import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthUser implements UserDetails {

  @Serial private static final long serialVersionUID = 525346235L;

  @Getter private final UUID id;

  private final String username;

  @JsonIgnore private final String password;

  private final Collection<? extends GrantedAuthority> authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public static AuthUser from(final User user) {
    return new AuthUser(
        user.id(),
        user.username(),
        user.password(),
        List.of(new SimpleGrantedAuthority("ROLE_" + user.role().name())));
  }
}
