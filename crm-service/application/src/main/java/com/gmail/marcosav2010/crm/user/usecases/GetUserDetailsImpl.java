package com.gmail.marcosav2010.crm.user.usecases;

import com.gmail.marcosav2010.crm.user.entities.User;
import com.gmail.marcosav2010.crm.user.exception.UserNotFound;
import com.gmail.marcosav2010.crm.user.ports.UserPort;
import java.util.UUID;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@CustomLog
@Component
@RequiredArgsConstructor
public class GetUserDetailsImpl implements GetUserDetails {

  private final UserPort userPort;

  @Override
  public User execute(final UUID id) {
    log.debug("Getting user details for id: {}", id);
    final var user =
        userPort.findById(id).orElseThrow(() -> new UserNotFound("User not found for id: " + id));

    if (!user.active()) {
      log.debug("User {} is not active", id);
      throw new UserNotFound("User not found for id: " + id);
    }

    return user;
  }
}
