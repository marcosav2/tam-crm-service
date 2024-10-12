package com.gmail.marcosav2010.crm.user.usecases;

import com.gmail.marcosav2010.crm.user.entities.User;
import com.gmail.marcosav2010.crm.user.exception.UserNotFound;
import com.gmail.marcosav2010.crm.user.ports.PasswordEncryptionPort;
import com.gmail.marcosav2010.crm.user.ports.UserPort;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@CustomLog
@Component
@RequiredArgsConstructor
public class UpdateUserImpl implements UpdateUser {

  private final UserPort userPort;

  private final PasswordEncryptionPort passwordEncryptionPort;

  @Override
  public User execute(final User user) {
    log.debug("Getting user details for id: {}", user.id());
    final User existingUser =
        userPort
            .findById(user.id())
            .orElseThrow(() -> new UserNotFound("User not found for id: " + user.id()));

    final String passwordToUpdate;
    if (StringUtils.hasText(user.password())) {
      user.validatePassword();
      passwordToUpdate = passwordEncryptionPort.process(user.password());
    } else {
      passwordToUpdate = existingUser.password();
    }

    final var userToUpdate =
        existingUser.toBuilder()
            .name(user.name())
            .surname(user.surname())
            .role(user.role())
            .password(passwordToUpdate)
            .build();

    log.debug("Updating user {}", userToUpdate);

    return userPort.update(userToUpdate);
  }
}
