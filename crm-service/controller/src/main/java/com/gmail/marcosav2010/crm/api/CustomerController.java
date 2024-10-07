package com.gmail.marcosav2010.crm.api;

import com.gmail.marcosav2010.crm.api.controller.CustomerApi;
import com.gmail.marcosav2010.crm.api.dto.CustomerDTO;
import com.gmail.marcosav2010.crm.api.dto.CustomerOverviewDTO;
import com.gmail.marcosav2010.crm.api.dto.ListCustomers200ResponseDTO;
import com.gmail.marcosav2010.crm.customer.usecases.*;
import com.gmail.marcosav2010.crm.user.usecases.ListActiveUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CustomerController implements CustomerApi {

  private final RegisterCustomer registerCustomer;
  private final DeleteCustomer deleteCustomer;
  private final DeleteCustomerProfileImage deleteCustomerProfileImage;
  private final GetCustomerDetails getCustomerDetails;
  private final ListActiveUsers listActiveUsers;
  private final UpdateCustomer updateCustomer;

  @Override
  public ResponseEntity<CustomerOverviewDTO> createCustomer(
      String name, String surname, MultipartFile profileImage) {
    return null;
  }

  @Override
  public ResponseEntity<Void> deleteCustomer(UUID id) {
    return null;
  }

  @Override
  public ResponseEntity<Void> deleteCustomerProfileImage(UUID id) {
    return null;
  }

  @Override
  public ResponseEntity<CustomerDTO> getCustomer(UUID id) {
    return null;
  }

  @Override
  public ResponseEntity<ListCustomers200ResponseDTO> listCustomers(Integer page, Integer size) {
    return null;
  }

  @Override
  public ResponseEntity<CustomerOverviewDTO> updateCustomer(
      UUID id, String name, String surname, MultipartFile profileImage) {
    return null;
  }
}
