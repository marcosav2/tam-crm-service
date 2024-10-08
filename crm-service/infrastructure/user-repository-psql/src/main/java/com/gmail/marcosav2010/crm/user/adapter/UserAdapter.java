package com.gmail.marcosav2010.crm.user.adapter;

import com.gmail.marcosav2010.crm.user.entities.User;
import com.gmail.marcosav2010.crm.user.ports.UserPort;
import com.gmail.marcosav2010.crm.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserPort {

  private final UserRepository repository;

  @Override
  public Optional<User> findById(UUID id) {
    return this.repository.findById(id);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return this.repository.findByUsername(username);
  }

  @Override
  public List<User> findActive(int page, int size) {
    return this.repository.findByActiveWithLimitAndOffset(true, size, page * size);
  }

  @Override
  public long countActive() {
    return this.repository.countByActive(true);
  }

  @Override
  public User update(User user) {
    repository.update(user);
    return user;
  }

  @Override
  public User register(User user) {
    final var userToInsert = user.toBuilder().id(UUID.randomUUID()).build();
    repository.insert(userToInsert);
    return userToInsert;
  }
}
