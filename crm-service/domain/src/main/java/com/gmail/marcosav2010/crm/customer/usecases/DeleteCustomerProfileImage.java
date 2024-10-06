package com.gmail.marcosav2010.crm.customer.usecases;

import java.util.UUID;

public interface DeleteCustomerProfileImage {

  void execute(UUID id, String user);
}
