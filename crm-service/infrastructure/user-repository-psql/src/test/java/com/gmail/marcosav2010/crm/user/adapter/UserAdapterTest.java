package com.gmail.marcosav2010.crm.user.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gmail.marcosav2010.crm.user.entities.User;
import com.gmail.marcosav2010.crm.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserAdapterTest {

  @Mock private UserRepository repository;

  @InjectMocks private UserAdapter adapter;

  @Test
  void findById_matches_notEmpty() {
    final User user =
        User.builder()
            .id(UUID.randomUUID())
            .username("uuuuu")
            .name("name")
            .surname("surname")
            .build();

    when(this.repository.findById(user.id())).thenReturn(Optional.of(user));

    final var optionalInc = this.adapter.findById(user.id());

    assertThat(optionalInc).isNotEmpty().contains(user);

    verify(this.repository).findById(user.id());
  }

  @Test
  void findById_noMatches_empty() {
    when(this.repository.findById(any())).thenReturn(Optional.empty());

    final var optionalInc = this.adapter.findById(UUID.randomUUID());

    assertThat(optionalInc).isEmpty();
  }

  @Test
  void findByUsername_matches_notEmpty() {
    final User user =
        User.builder()
            .id(UUID.randomUUID())
            .username("uuuuu")
            .name("name")
            .surname("surname")
            .build();

    when(this.repository.findByUsername(user.username())).thenReturn(Optional.of(user));

    final var optionalInc = this.adapter.findByUsername(user.username());

    assertThat(optionalInc).isNotEmpty().contains(user);

    verify(this.repository).findByUsername(user.username());
  }

  @Test
  void findActive_matches() {
    final var users =
        List.of(
            User.builder()
                .username("uuuuu")
                .id(UUID.randomUUID())
                .name("name")
                .surname("surname")
                .build(),
            User.builder()
                .username("uuuuu")
                .id(UUID.randomUUID())
                .name("name")
                .surname("surname")
                .build());

    when(this.repository.findByActiveWithLimitAndOffset(true, 10, 0)).thenReturn(users);

    final var result = this.adapter.findActive(0, 10);

    assertThat(result).isNotEmpty().containsExactlyElementsOf(users);

    verify(this.repository).findByActiveWithLimitAndOffset(true, 10, 0);
  }

  @Test
  void countActive_matches() {
    when(this.repository.countByActive(true)).thenReturn(10L);

    final var result = this.adapter.countActive();

    assertThat(result).isEqualTo(10L);

    verify(this.repository).countByActive(true);
  }

  @Test
  void update() {
    final User user =
        User.builder()
            .username("uuuuu")
            .id(UUID.randomUUID())
            .name("name")
            .surname("surname")
            .build();

    final var updated = this.adapter.update(user);

    assertThat(updated).isNotNull();

    verify(this.repository).update(updated);
  }

  @Test
  void register() {
    final User user = User.builder().username("uuuuu").name("name").surname("surname").build();

    final var registered = this.adapter.register(user);

    assertThat(registered).isNotNull();
    assertThat(registered.id()).isNotNull();

    verify(this.repository).insert(registered);
  }
}
