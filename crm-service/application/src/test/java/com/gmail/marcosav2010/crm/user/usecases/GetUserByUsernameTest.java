package com.gmail.marcosav2010.crm.user.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.gmail.marcosav2010.crm.user.entities.User;
import com.gmail.marcosav2010.crm.user.exception.UserNotFound;
import com.gmail.marcosav2010.crm.user.ports.UserPort;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetUserByUsernameTest {

  @Mock private UserPort userPort;

  @InjectMocks private GetUserByUsernameImpl getUserByUsername;

  @Test
  void execute_notExisting_throwNotFound() {
    final var username = "aaa123445";
    when(userPort.findByUsername(username)).thenReturn(Optional.empty());

    assertThrows(UserNotFound.class, () -> getUserByUsername.execute(username));

    verify(userPort).findByUsername(username);
  }

  @Test
  void execute_existingDeactivated_throwNotFound() {
    final var username = "aaa123445";
    final var customer =
        User.builder()
            .id(UUID.randomUUID())
            .username(username)
            .name("name2")
            .surname("surname2")
            .active(false)
            .build();

    when(userPort.findByUsername(username)).thenReturn(Optional.of(customer));

    assertThrows(UserNotFound.class, () -> getUserByUsername.execute(username));

    verify(userPort).findByUsername(username);
  }

  @Test
  void execute_success_getUser() {
    final var username = "aaa123445";
    final var user =
        User.builder()
            .id(UUID.randomUUID())
            .username(username)
            .name("name2")
            .surname("surname2")
            .active(true)
            .build();

    when(userPort.findByUsername(username)).thenReturn(Optional.of(user));

    final var result = getUserByUsername.execute(username);
    assertThat(result).isEqualTo(user);

    verify(userPort).findByUsername(username);
  }
}
