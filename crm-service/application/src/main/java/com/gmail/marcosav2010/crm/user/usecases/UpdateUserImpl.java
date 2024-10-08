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
public class UpdateUserImpl implements UpdateUser {

  private final UserPort userPort;

  @Override
  public User execute(final User user) {
    log.debug("Getting user details for id: {}", user.id());
    final User existingUser =
        userPort
            .findById(user.id())
            .orElseThrow(() -> new UserNotFound("User not found for id: " + user.id()));

    final var userToUpdate =
        existingUser.toBuilder()
            .name(user.name())
            .surname(user.surname())
            .role(user.role())
            .password(user.password())
            .build();

    log.debug("Updating user {}", userToUpdate);

    return userPort.update(userToUpdate);
  }
}
