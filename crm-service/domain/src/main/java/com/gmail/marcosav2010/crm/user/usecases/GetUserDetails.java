package com.gmail.marcosav2010.crm.user.usecases;

import com.gmail.marcosav2010.crm.user.entities.User;
import java.util.UUID;

public interface GetUserDetails {

  User execute(UUID id);
}
