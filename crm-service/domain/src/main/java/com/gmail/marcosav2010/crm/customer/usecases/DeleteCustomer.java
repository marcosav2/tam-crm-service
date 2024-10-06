package com.gmail.marcosav2010.crm.customer.usecases;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import java.util.UUID;

public interface DeleteCustomer {

  Customer execute(UUID id);
}
