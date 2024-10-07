package com.gmail.marcosav2010.crm.user.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.gmail.marcosav2010.crm.user.entities.User;
import com.gmail.marcosav2010.crm.user.exception.UserNotFound;
import com.gmail.marcosav2010.crm.user.ports.UserPort;
import com.gmail.marcosav2010.crm.user.usecases.DeleteUserImpl;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteUserTest {

  @Mock private UserPort userPort;

  @InjectMocks private DeleteUserImpl deleteUser;

  @Test
  void execute_notExisting_throwNotFound() {
    final UUID id = UUID.randomUUID();
    when(userPort.findById(id)).thenReturn(Optional.empty());

    assertThrows(UserNotFound.class, () -> deleteUser.execute(id));

    verify(userPort).findById(id);
    verifyNoMoreInteractions(userPort);
  }

  @Test
  void execute_success_updateDeactivateUser() {
    final var user =
        User.builder()
            .id(UUID.randomUUID())
            .username("aaa1234")
            .name("name")
            .surname("surname")
            .active(true)
            .build();

    when(userPort.findById(user.id())).thenReturn(Optional.of(user));

    final var updatedUser = user.toBuilder().active(false).build();
    when(userPort.update(updatedUser)).thenReturn(updatedUser.toBuilder().build());

    deleteUser.execute(user.id());

    verify(userPort).findById(user.id());
    verify(userPort).update(updatedUser);
  }
}
