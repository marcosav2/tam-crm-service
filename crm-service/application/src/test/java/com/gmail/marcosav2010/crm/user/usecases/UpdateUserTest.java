package com.gmail.marcosav2010.crm.user.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.gmail.marcosav2010.crm.user.entities.User;
import com.gmail.marcosav2010.crm.user.entities.UserRole;
import com.gmail.marcosav2010.crm.user.exception.UserNotFound;
import com.gmail.marcosav2010.crm.user.ports.PasswordEncryptionPort;
import com.gmail.marcosav2010.crm.user.ports.UserPort;
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

  @Mock private PasswordEncryptionPort passwordEncryptionPort;

  @InjectMocks private UpdateUserImpl updateUser;

  @Test
  void execute_notExisting_throwNotFound() {
    final UUID id = UUID.randomUUID();
    final var user = User.builder().id(id).build();
    when(userPort.findById(id)).thenReturn(Optional.empty());

    assertThrows(UserNotFound.class, () -> updateUser.execute(user));

    verify(userPort).findById(id);
    verifyNoMoreInteractions(userPort);
    verifyNoInteractions(passwordEncryptionPort);
  }

  @Test
  void execute_updatingPassword_updateUser() {
    final var user =
        User.builder()
            .id(UUID.randomUUID())
            .username("aaa1234454")
            .password("newPassword")
            .name("name2")
            .surname("surname2")
            .role(UserRole.USER)
            .build();
    final var existingUser =
        user.toBuilder()
            .username("aaa1234")
            .password("encryptedOldPassword")
            .name("name")
            .surname("surname")
            .active(true)
            .role(UserRole.ADMIN)
            .build();

    when(userPort.findById(user.id())).thenReturn(Optional.of(existingUser));
    when(userPort.update(any())).thenReturn(user);
    when(passwordEncryptionPort.process(any())).thenReturn("encryptedNewPassword");

    final var result = updateUser.execute(user);

    assertThat(result).isEqualTo(user);

    verify(userPort).findById(user.id());
    verify(userPort)
        .update(
            user.toBuilder()
                .username(existingUser.username())
                .password("encryptedNewPassword")
                .active(existingUser.active())
                .build());
    verify(passwordEncryptionPort).process("newPassword");
  }

  @Test
  void execute_notUpdatingPassword_updateUser() {
    final var user =
        User.builder()
            .id(UUID.randomUUID())
            .username("aaa1234454")
            .password("")
            .name("name2")
            .surname("surname2")
            .role(UserRole.USER)
            .build();
    final var existingUser =
        user.toBuilder()
            .username("aaa1234")
            .password("encryptedPassword")
            .name("name")
            .surname("surname")
            .active(true)
            .role(UserRole.ADMIN)
            .build();

    when(userPort.findById(user.id())).thenReturn(Optional.of(existingUser));
    when(userPort.update(any())).thenReturn(user);

    final var result = updateUser.execute(user);

    assertThat(result).isEqualTo(user);

    verify(userPort).findById(user.id());
    verify(userPort)
        .update(
            user.toBuilder()
                .username(existingUser.username())
                .password("encryptedPassword")
                .active(existingUser.active())
                .build());
    verifyNoInteractions(passwordEncryptionPort);
  }
}
