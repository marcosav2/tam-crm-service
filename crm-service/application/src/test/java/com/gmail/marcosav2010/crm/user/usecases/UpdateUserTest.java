package com.gmail.marcosav2010.crm.user.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.gmail.marcosav2010.crm.user.entities.User;
import com.gmail.marcosav2010.crm.user.exception.UserNotFound;
import com.gmail.marcosav2010.crm.user.ports.UserPort;
import com.gmail.marcosav2010.crm.user.usecases.UpdateUserImpl;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateUserTest {

  @Mock private UserPort userPort;

  @InjectMocks private UpdateUserImpl updateUser;

  @Test
  void execute_notExisting_throwNotFound() {
    final UUID id = UUID.randomUUID();
    final var user = User.builder().id(id).build();
    when(userPort.findById(id)).thenReturn(Optional.empty());

    assertThrows(UserNotFound.class, () -> updateUser.execute(user));

    verify(userPort).findById(id);
    verifyNoMoreInteractions(userPort);
  }

  @Test
  void execute_success_updateUser() {
    final var user =
        User.builder()
            .id(UUID.randomUUID())
            .username("aaa1234454")
            .name("name2")
            .surname("surname2")
            .build();
    final var existingUser =
        user.toBuilder().username("aaa1234").name("name").surname("surname").active(true).build();

    when(userPort.findById(user.id())).thenReturn(Optional.of(existingUser));
    when(userPort.update(any())).thenReturn(user);

    final var result = updateUser.execute(user);

    assertThat(result).isEqualTo(user);

    verify(userPort).findById(user.id());
    verify(userPort)
        .update(
            user.toBuilder()
                .username(existingUser.username())
                .active(existingUser.active())
                .build());
  }
}
