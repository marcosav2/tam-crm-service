package com.gmail.marcosav2010.crm.api.mappers;

import com.gmail.marcosav2010.crm.api.dto.CreateUserRequestDTO;
import com.gmail.marcosav2010.crm.api.dto.UpdateUserRequestDTO;
import com.gmail.marcosav2010.crm.api.dto.UserRoleDTO;
import com.gmail.marcosav2010.crm.shared.entities.Paged;
import com.gmail.marcosav2010.crm.user.entities.User;
import com.gmail.marcosav2010.crm.user.entities.UserRole;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ControllerUserMapperTest {

  private final ControllerUserMapper controllerUserMapper =
      Mappers.getMapper(ControllerUserMapper.class);

  @Test
  void map() {
    final var user =
        User.builder()
            .id(UUID.randomUUID())
            .username("username")
            .password("password")
            .name("name")
            .surname("surname")
            .active(true)
            .role(UserRole.ADMIN)
            .build();

    final var userDTO = controllerUserMapper.map(user);

    Assertions.assertEquals(user.id(), userDTO.getId());
    Assertions.assertEquals(user.username(), userDTO.getUsername());
    Assertions.assertEquals(user.name(), userDTO.getName());
    Assertions.assertEquals(user.surname(), userDTO.getSurname());
    Assertions.assertEquals(user.role().name(), userDTO.getRole().name());
  }

  @Test
  void mapCreate() {
    final var userDTO = new CreateUserRequestDTO();
    userDTO.setUsername("username");
    userDTO.setPassword("password");
    userDTO.setName("name");
    userDTO.setSurname("surname");
    userDTO.setRole(UserRoleDTO.ADMIN);

    final var user = controllerUserMapper.map(userDTO);

    Assertions.assertEquals(userDTO.getUsername(), user.username());
    Assertions.assertEquals(userDTO.getPassword(), user.password());
    Assertions.assertEquals(userDTO.getName(), user.name());
    Assertions.assertEquals(userDTO.getSurname(), user.surname());
    Assertions.assertEquals(userDTO.getRole().name(), user.role().name());
  }

  @Test
  void mapUpdate() {
    final var userDTO = new UpdateUserRequestDTO();
    userDTO.setPassword("password");
    userDTO.setName("name");
    userDTO.setSurname("surname");
    userDTO.setRole(UserRoleDTO.ADMIN);

    final var user = controllerUserMapper.map(UUID.randomUUID(), userDTO);

    Assertions.assertEquals(userDTO.getPassword(), user.password());
    Assertions.assertEquals(userDTO.getName(), user.name());
    Assertions.assertEquals(userDTO.getSurname(), user.surname());
    Assertions.assertEquals(userDTO.getRole().name(), user.role().name());
  }

  @Test
  void mapPaged() {
    final var user =
        User.builder()
            .id(UUID.randomUUID())
            .username("username")
            .password("password")
            .name("name")
            .surname("surname")
            .active(true)
            .role(UserRole.ADMIN)
            .build();

    final var paged = new Paged<>(List.of(user), 1);

    final var userDTO = controllerUserMapper.map(paged);

    Assertions.assertEquals(1, userDTO.getResults());
    Assertions.assertEquals(1, userDTO.getData().size());
    Assertions.assertEquals(user.id(), userDTO.getData().getFirst().getId());
    Assertions.assertEquals(user.username(), userDTO.getData().getFirst().getUsername());
    Assertions.assertEquals(user.name(), userDTO.getData().getFirst().getName());
    Assertions.assertEquals(user.surname(), userDTO.getData().getFirst().getSurname());
    Assertions.assertEquals(user.role().name(), userDTO.getData().getFirst().getRole().name());
  }
}
