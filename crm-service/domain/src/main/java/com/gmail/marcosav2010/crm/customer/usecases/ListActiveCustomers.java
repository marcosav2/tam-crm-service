package com.gmail.marcosav2010.crm.customer.usecases;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.customer.entities.CustomerListRequest;
import com.gmail.marcosav2010.crm.shared.entities.Paged;

public interface ListActiveCustomers {

  Paged<Customer> execute(CustomerListRequest request);
}
