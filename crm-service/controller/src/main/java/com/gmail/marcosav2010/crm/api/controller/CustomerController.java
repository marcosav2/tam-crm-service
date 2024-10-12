package com.gmail.marcosav2010.crm.api.controller;

import com.gmail.marcosav2010.crm.api.dto.CustomerDTO;
import com.gmail.marcosav2010.crm.api.dto.CustomerOverviewDTO;
import com.gmail.marcosav2010.crm.api.dto.ListCustomers200ResponseDTO;
import com.gmail.marcosav2010.crm.api.helpers.FileHelper;
import com.gmail.marcosav2010.crm.api.mappers.ControllerCustomerMapper;
import com.gmail.marcosav2010.crm.api.security.AuthCtx;
import com.gmail.marcosav2010.crm.api.validation.Validate;
import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.customer.entities.CustomerListRequest;
import com.gmail.marcosav2010.crm.customer.usecases.*;
import com.gmail.marcosav2010.crm.shared.entities.Page;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class CustomerController implements CustomerApi {

  private final ControllerCustomerMapper mapper;

  private final RegisterCustomer registerCustomer;
  private final DeleteCustomer deleteCustomer;
  private final DeleteCustomerProfileImage deleteCustomerProfileImage;
  private final GetCustomerDetails getCustomerDetails;
  private final ListActiveCustomers listActiveCustomers;
  private final UpdateCustomer updateCustomer;

  @Override
  public ResponseEntity<CustomerOverviewDTO> createCustomer(
      String name, String surname, MultipartFile profileImage) {
    Validate.imageType(profileImage);
    final var customer = Customer.builder().name(name).surname(surname).build();
    final var file = FileHelper.toDomain(profileImage);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(mapper.mapOverview(registerCustomer.execute(customer, file, AuthCtx.getUser())));
  }

  @Override
  public ResponseEntity<Void> deleteCustomer(UUID id) {
    deleteCustomer.execute(id, AuthCtx.getUser());
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> deleteCustomerProfileImage(UUID id) {
    deleteCustomerProfileImage.execute(id, AuthCtx.getUser());
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<CustomerDTO> getCustomer(UUID id) {
    return ResponseEntity.ok(mapper.map(getCustomerDetails.execute(id)));
  }

  @Override
  public ResponseEntity<ListCustomers200ResponseDTO> listCustomers(Integer page, Integer size) {
    System.out.println(AuthCtx.getUser());
    final var request =
        CustomerListRequest.builder().page(Page.builder().page(page).size(size).build()).build();
    return ResponseEntity.ok(mapper.map(listActiveCustomers.execute(request)));
  }

  @Override
  public ResponseEntity<CustomerOverviewDTO> updateCustomer(
      UUID id, String name, String surname, MultipartFile profileImage) {
    Validate.imageType(profileImage);
    final var customer = Customer.builder().id(id).name(name).surname(surname).build();
    final var file = FileHelper.toDomain(profileImage);
    return ResponseEntity.ok(
        mapper.mapOverview(updateCustomer.execute(customer, file, AuthCtx.getUser())));
  }
}
