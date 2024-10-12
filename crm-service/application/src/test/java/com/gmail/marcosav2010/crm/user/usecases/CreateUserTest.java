package com.gmail.marcosav2010.crm.user.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gmail.marcosav2010.crm.user.entities.User;
import com.gmail.marcosav2010.crm.user.entities.UserRole;
import com.gmail.marcosav2010.crm.user.exception.UsernameAlreadyUsed;
import com.gmail.marcosav2010.crm.user.ports.PasswordEncryptionPort;
import com.gmail.marcosav2010.crm.user.ports.UserPort;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateUserTest {

  @Mock private UserPort userPort;

  @Mock private PasswordEncryptionPort passwordEncryptionPort;

  @InjectMocks private CreateUserImpl createUser;

  @Test
  void execute_success_createUser() {
    final var user =
        User.builder()
            .username("aaa1234")
            .password("password")
            .name("name")
            .surname("email")
            .role(UserRole.USER)
            .build();

    when(userPort.findByUsername(any())).thenReturn(Optional.empty());
    when(userPort.register(any())).thenReturn(user);
    when(passwordEncryptionPort.process(any())).thenReturn("encrypted");

    final var result = createUser.execute(user);

    assertThat(result).isEqualTo(user);

    verify(userPort).register(user.toBuilder().password("encrypted").active(true).build());
    verify(passwordEncryptionPort).process("password");
  }

  @Test
  void execute_alreadyUsed_createUser() {
    final var user =
        User.builder()
            .username("aaa1234")
            .name("name")
            .surname("email")
            .role(UserRole.USER)
            .build();

    when(userPort.findByUsername(any())).thenReturn(Optional.of(User.builder().build()));

    assertThrows(UsernameAlreadyUsed.class, () -> createUser.execute(user));
  }
}
