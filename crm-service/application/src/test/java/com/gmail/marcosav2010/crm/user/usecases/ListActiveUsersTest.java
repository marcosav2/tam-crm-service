package com.gmail.marcosav2010.crm.user.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.gmail.marcosav2010.crm.shared.entities.Page;
import com.gmail.marcosav2010.crm.user.entities.User;
import com.gmail.marcosav2010.crm.user.entities.UserListRequest;
import com.gmail.marcosav2010.crm.user.ports.UserPort;
import com.gmail.marcosav2010.crm.user.usecases.ListActiveUsersImpl;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListActiveUsersTest {

  @Mock private UserPort userPort;

  @InjectMocks private ListActiveUsersImpl listActiveUsers;

  @Test
  void execute_hugePageSize_illegalArg() {
    final var request =
        UserListRequest.builder().page(Page.builder().page(1).size(1000).build()).build();

    assertThrows(IllegalArgumentException.class, () -> listActiveUsers.execute(request));

    verifyNoInteractions(userPort);
  }

  @Test
  void execute_success_getUsers() {
    final var user =
        User.builder()
            .id(UUID.randomUUID())
            .username("aaa1234454")
            .name("name2")
            .surname("surname2")
            .build();

    final var request =
        UserListRequest.builder().page(Page.builder().page(1).size(10).build()).build();

    when(userPort.findActive(0, 10)).thenReturn(List.of(user));
    when(userPort.countActive()).thenReturn(1L);

    final var results = listActiveUsers.execute(request);

    assertThat(results)
        .satisfies(
            r -> {
              assertThat(r.results()).isEqualTo(1);
              assertThat(r.data()).containsExactly(user);
            });

    verify(userPort).findActive(0, 10);
    verify(userPort).countActive();
  }
}
