package com.gmail.marcosav2010.crm.customer.usecases;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.customer.exceptions.CustomerNotFound;
import com.gmail.marcosav2010.crm.customer.ports.CustomerPort;
import com.gmail.marcosav2010.crm.customer.ports.ProfileImagePort;
import java.util.UUID;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@CustomLog
@Component
@RequiredArgsConstructor
public class DeleteCustomerProfileImageImpl implements DeleteCustomerProfileImage {

  private final CustomerPort customerPort;

  private final ProfileImagePort profileImagePort;

  @Override
  public void execute(UUID id, String user) {
    log.debug("Getting customer details for id: {}", id);
    final Customer customer = customerPort.findById(id);

    if (customer == null) {
      throw new CustomerNotFound("Customer not found for id: " + id);
    }

    final var currentImageKey = customer.profileImageUrl();
    if (currentImageKey == null) {
      log.warn("No profile image found for customer: {}", id);
      return;
    }

    log.debug("Deleting image {} for user {}", currentImageKey, id);
    profileImagePort.delete(currentImageKey);

    final var updatedCustomer = customer.toBuilder().profileImageUrl(null).build();
    customerPort.update(updatedCustomer, user);
  }
}
