package com.gmail.marcosav2010.crm.customer.ports;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerPort {

  Optional<Customer> findById(UUID id);

  List<Customer> findActive(int page, int size);

  long countActive();

  Customer update(Customer customer, String user);

  Customer register(Customer customer, String user);
}
