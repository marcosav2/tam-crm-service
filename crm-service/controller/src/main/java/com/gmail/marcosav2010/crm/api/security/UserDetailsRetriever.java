package com.gmail.marcosav2010.crm.api.security;

import com.gmail.marcosav2010.crm.user.exception.UserNotFound;
import com.gmail.marcosav2010.crm.user.usecases.GetUserByUsername;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class UserDetailsRetriever implements UserDetailsService {

  private final GetUserByUsername getUserByUsername;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      return AuthUser.from(getUserByUsername.execute(username));
    } catch (final UserNotFound e) {
      throw new UsernameNotFoundException("User not found", e);
    }
  }
}
