package com.gmail.marcosav2010.crm.customer.usecases;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import java.io.InputStream;

public interface UpdateCustomer {

  Customer execute(Customer customer, InputStream profileImage);
}
