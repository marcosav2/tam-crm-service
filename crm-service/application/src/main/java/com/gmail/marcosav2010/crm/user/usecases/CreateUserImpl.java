package com.gmail.marcosav2010.crm.user.usecases;

import com.gmail.marcosav2010.crm.user.entities.User;
import com.gmail.marcosav2010.crm.user.exception.UsernameAlreadyUsed;
import com.gmail.marcosav2010.crm.user.ports.PasswordEncryptionPort;
import com.gmail.marcosav2010.crm.user.ports.UserPort;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@CustomLog
@Component
@RequiredArgsConstructor
public class CreateUserImpl implements CreateUser {

  private final UserPort userPort;

  private final PasswordEncryptionPort passwordEncryptionPort;

  @Override
  public User execute(final User user) {
    log.debug("Creating user {}", user);

    final var existingUsernameUser = userPort.findByUsername(user.username());
    if (existingUsernameUser.isPresent()) {
      throw new UsernameAlreadyUsed("Username already exists: " + user.username());
    }

    user.validatePassword();

    final var encryptedPassword = passwordEncryptionPort.process(user.password());
    final var userToSave = user.toBuilder().password(encryptedPassword).active(true).build();
    return userPort.register(userToSave);
  }
}
