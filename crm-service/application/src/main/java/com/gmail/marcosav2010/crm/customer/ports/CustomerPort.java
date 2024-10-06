package com.gmail.marcosav2010.crm.customer.ports;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import java.util.List;
import java.util.UUID;

public interface CustomerPort {

  Customer findById(UUID id);

  List<Customer> findActive(int page, int size);

  int countActive();

  Customer update(Customer customer, String user);

  Customer register(Customer customer, String user);
}
