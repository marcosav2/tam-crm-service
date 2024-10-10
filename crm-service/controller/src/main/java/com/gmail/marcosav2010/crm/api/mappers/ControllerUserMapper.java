package com.gmail.marcosav2010.crm.api.mappers;

import com.gmail.marcosav2010.crm.api.dto.CreateUserRequestDTO;
import com.gmail.marcosav2010.crm.api.dto.ListUsers200ResponseDTO;
import com.gmail.marcosav2010.crm.api.dto.UpdateUserRequestDTO;
import com.gmail.marcosav2010.crm.api.dto.UserDTO;
import com.gmail.marcosav2010.crm.shared.entities.Paged;
import com.gmail.marcosav2010.crm.user.entities.User;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ControllerUserMapper {

  UserDTO map(User user);

  User map(CreateUserRequestDTO dto);

  @Mapping(target = "username", ignore = true)
  User map(UUID id, UpdateUserRequestDTO updateUserRequestDTO);

  ListUsers200ResponseDTO map(Paged<User> users);
}
