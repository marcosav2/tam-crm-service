package com.gmail.marcosav2010.crm.api;

import com.gmail.marcosav2010.crm.api.controller.UserApi;
import com.gmail.marcosav2010.crm.api.dto.CreateUserRequestDTO;
import com.gmail.marcosav2010.crm.api.dto.ListUsers200ResponseDTO;
import com.gmail.marcosav2010.crm.api.dto.UpdateUserRequestDTO;
import com.gmail.marcosav2010.crm.api.dto.UserDTO;
import com.gmail.marcosav2010.crm.user.usecases.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

  private final CreateUser createUser;
  private final DeleteUser deleteUser;
  private final UpdateUser updateUser;
  private final ListActiveUsers listUsers;
  private final GetUserDetails getUser;

  @Override
  public ResponseEntity<UserDTO> createUser(CreateUserRequestDTO createUserRequestDTO) {
    return null;
  }

  @Override
  public ResponseEntity<Void> deleteUser(UUID id) {
    return null;
  }

  @Override
  public ResponseEntity<UserDTO> getUser(UUID id) {
    return null;
  }

  @Override
  public ResponseEntity<ListUsers200ResponseDTO> listUsers(Integer page, Integer size) {
    return null;
  }

  @Override
  public ResponseEntity<UserDTO> updateUser(UUID id, UpdateUserRequestDTO updateUserRequestDTO) {
    return null;
  }
}
