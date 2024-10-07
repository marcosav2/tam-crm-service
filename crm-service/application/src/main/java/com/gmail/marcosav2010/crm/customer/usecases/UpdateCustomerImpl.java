package com.gmail.marcosav2010.crm.customer.usecases;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.customer.exceptions.CustomerNotFound;
import com.gmail.marcosav2010.crm.customer.ports.CustomerPort;
import com.gmail.marcosav2010.crm.customer.ports.ProfileImagePort;
import java.io.InputStream;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@CustomLog
@Component
@RequiredArgsConstructor
public class UpdateCustomerImpl implements UpdateCustomer {

  private final CustomerPort customerPort;

  private final ProfileImagePort profileImagePort;

  @Override
  public Customer execute(
      final Customer customer, final InputStream profileImage, final String user) {
    log.debug("Getting customer details for id: {}", customer.id());
    final Customer existingCustomer =
        customerPort
            .findById(customer.id())
            .orElseThrow(() -> new CustomerNotFound("Customer not found for id: " + customer.id()));

    final String imageKey = updateProfileImage(existingCustomer, profileImage);

    log.debug("Updating customer {}", customer);
    final var customerToUpdate = customer.toBuilder().profileImageUrl(imageKey).build();

    try {
      return customerPort.update(customerToUpdate, user);

    } catch (Exception e) {
      if (profileImage != null) {
        profileImagePort.delete(imageKey);
      }
      throw e;
    }
  }

  private String updateProfileImage(
      final Customer existingCustomer, final InputStream profileImage) {
    String imageKey = existingCustomer.profileImageUrl();
    if (profileImage != null) {
      if (imageKey != null) {
        profileImagePort.delete(imageKey);
      }
      imageKey = profileImagePort.save(profileImage);
    }
    return imageKey;
  }
}
