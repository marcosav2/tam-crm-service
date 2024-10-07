package com.gmail.marcosav2010.crm.customer.usecases;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.customer.ports.CustomerPort;
import com.gmail.marcosav2010.crm.customer.ports.ProfileImagePort;
import java.io.InputStream;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@CustomLog
@Component
@RequiredArgsConstructor
public class RegisterCustomerImpl implements RegisterCustomer {

  private final CustomerPort customerPort;

  private final ProfileImagePort profileImagePort;

  @Override
  public Customer execute(
      final Customer customer, final InputStream profileImage, final String user) {
    log.debug("Creating customer {}", customer);

    String imageKey = null;
    if (profileImage != null) {
      imageKey = profileImagePort.save(profileImage);
    }

    final var customerToSave = customer.toBuilder().profileImageUrl(imageKey).build();

    try {
      return customerPort.register(customerToSave, user);

    } catch (Exception e) {
      if (imageKey != null) {
        profileImagePort.delete(imageKey);
      }
      throw e;
    }
  }
}
