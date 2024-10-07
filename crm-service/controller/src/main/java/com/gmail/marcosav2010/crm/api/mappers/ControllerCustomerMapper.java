package com.gmail.marcosav2010.crm.api.mappers;

import com.gmail.marcosav2010.crm.api.dto.CustomerDTO;
import com.gmail.marcosav2010.crm.api.dto.CustomerOverviewDTO;
import com.gmail.marcosav2010.crm.api.dto.ListCustomers200ResponseDTO;
import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.shared.entities.Paged;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ControllerCustomerMapper {

  CustomerDTO map(Customer customer);

  CustomerOverviewDTO mapOverview(Customer customer);

  ListCustomers200ResponseDTO map(Paged<Customer> customers);
}
