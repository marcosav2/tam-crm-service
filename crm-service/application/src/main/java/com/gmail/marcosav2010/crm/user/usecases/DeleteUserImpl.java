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
public class DeleteUserImpl implements DeleteUser {

  private final UserPort userPort;

  @Override
  public void execute(UUID id) {
    log.debug("Getting user details for id: {}", id);
    final User existingUser =
        userPort.findById(id).orElseThrow(() -> new UserNotFound("User not found for id: " + id));

    log.debug("Deleting (deactivating) user {}", id);

    final var deletedUser = existingUser.toBuilder().active(false).build();
    userPort.update(deletedUser);
  }
}
