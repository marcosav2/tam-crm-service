package com.gmail.marcosav2010.crm.user.usecases;

import com.gmail.marcosav2010.crm.shared.entities.Paged;
import com.gmail.marcosav2010.crm.user.entities.User;
import com.gmail.marcosav2010.crm.user.entities.UserListRequest;

public interface ListActiveUsers {

  Paged<User> execute(UserListRequest request);
}
