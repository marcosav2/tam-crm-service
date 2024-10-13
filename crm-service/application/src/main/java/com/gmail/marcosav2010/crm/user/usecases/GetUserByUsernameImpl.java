package com.gmail.marcosav2010.crm.user.usecases;

import com.gmail.marcosav2010.crm.user.entities.User;
import com.gmail.marcosav2010.crm.user.exception.UserNotFound;
import com.gmail.marcosav2010.crm.user.ports.UserPort;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@CustomLog
@Component
@RequiredArgsConstructor
public class GetUserByUsernameImpl implements GetUserByUsername {

  private final UserPort userPort;

  @Override
  public User execute(final String username) {
    log.debug("Getting user details for user: {}", username);

    return userPort
        .findByUsername(username)
        .filter(User::active)
        .orElseThrow(
            () -> new UserNotFound("User not found or inactive for username: " + username));
  }
}
