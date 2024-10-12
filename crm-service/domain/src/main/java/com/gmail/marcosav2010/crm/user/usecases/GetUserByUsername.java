package com.gmail.marcosav2010.crm.user.usecases;

import com.gmail.marcosav2010.crm.user.entities.User;

public interface GetUserByUsername {

  User execute(String username);
}
