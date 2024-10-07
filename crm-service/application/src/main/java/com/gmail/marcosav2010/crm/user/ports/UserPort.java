package com.gmail.marcosav2010.crm.user.ports;

import com.gmail.marcosav2010.crm.user.entities.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserPort {

  Optional<User> findById(UUID id);

  Optional<User> findByUsername(String username);

  List<User> findActive(int page, int size);

  long countActive();

  User update(User user);

  User register(User user);
}
