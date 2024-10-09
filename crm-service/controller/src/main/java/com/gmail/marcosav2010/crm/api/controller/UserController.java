package com.gmail.marcosav2010.crm.api.controller;

import com.gmail.marcosav2010.crm.api.dto.CreateUserRequestDTO;
import com.gmail.marcosav2010.crm.api.dto.ListUsers200ResponseDTO;
import com.gmail.marcosav2010.crm.api.dto.UpdateUserRequestDTO;
import com.gmail.marcosav2010.crm.api.dto.UserDTO;
import com.gmail.marcosav2010.crm.api.mappers.ControllerUserMapper;
import com.gmail.marcosav2010.crm.shared.entities.Page;
import com.gmail.marcosav2010.crm.user.entities.UserListRequest;
import com.gmail.marcosav2010.crm.user.usecases.*;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

  private final ControllerUserMapper mapper;

  private final CreateUser createUser;
  private final DeleteUser deleteUser;
  private final UpdateUser updateUser;
  private final ListActiveUsers listUsers;
  private final GetUserDetails getUser;

  @Override
  public ResponseEntity<UserDTO> createUser(CreateUserRequestDTO createUserRequestDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(mapper.map(createUser.execute(mapper.map(createUserRequestDTO))));
  }

  @Override
  public ResponseEntity<Void> deleteUser(UUID id) {
    deleteUser.execute(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<UserDTO> getUser(UUID id) {
    return ResponseEntity.ok(mapper.map(getUser.execute(id)));
  }

  @Override
  public ResponseEntity<ListUsers200ResponseDTO> listUsers(Integer page, Integer size) {
    final var request =
        UserListRequest.builder().page(Page.builder().page(page).size(size).build()).build();
    return ResponseEntity.ok(mapper.map(listUsers.execute(request)));
  }

  @Override
  public ResponseEntity<UserDTO> updateUser(UUID id, UpdateUserRequestDTO updateUserRequestDTO) {
    return ResponseEntity.ok(mapper.map(updateUser.execute(mapper.map(id, updateUserRequestDTO))));
  }
}
