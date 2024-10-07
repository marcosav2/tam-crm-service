package com.gmail.marcosav2010.crm.user.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.gmail.marcosav2010.crm.user.entities.User;
import com.gmail.marcosav2010.crm.user.exception.UserNotFound;
import com.gmail.marcosav2010.crm.user.ports.UserPort;
import com.gmail.marcosav2010.crm.user.usecases.GetUserDetailsImpl;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetUserDetailsTest {

  @Mock private UserPort userPort;

  @InjectMocks private GetUserDetailsImpl getUserDetails;

  @Test
  void execute_notExisting_throwNotFound() {
    final UUID id = UUID.randomUUID();
    when(userPort.findById(id)).thenReturn(Optional.empty());

    assertThrows(UserNotFound.class, () -> getUserDetails.execute(id));

    verify(userPort).findById(id);
  }

  @Test
  void execute_success_getUser() {
    final var id = UUID.randomUUID();
    final var user =
        User.builder()
            .id(UUID.randomUUID())
            .username("aaa1234454")
            .name("name2")
            .surname("surname2")
            .build();

    when(userPort.findById(id)).thenReturn(Optional.of(user));

    final var result = getUserDetails.execute(id);
    assertThat(result).isEqualTo(user);

    verify(userPort).findById(id);
  }
}
