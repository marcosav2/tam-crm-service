package com.gmail.marcosav2010.crm.customer.usecases;

import java.util.UUID;

public interface DeleteCustomer {

  void execute(UUID id, String user);
}
