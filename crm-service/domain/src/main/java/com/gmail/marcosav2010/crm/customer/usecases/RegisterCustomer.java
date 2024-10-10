package com.gmail.marcosav2010.crm.customer.usecases;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.shared.entities.UploadFile;

public interface RegisterCustomer {

  Customer execute(Customer customer, UploadFile profileImage, String user);
}
