package com.gmail.marcosav2010.crm.customer.usecases;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.customer.exceptions.CustomerNotFound;
import com.gmail.marcosav2010.crm.customer.ports.CustomerPort;
import com.gmail.marcosav2010.crm.customer.ports.ProfileImageURLProviderPort;
import java.util.UUID;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@CustomLog
@Component
@RequiredArgsConstructor
public class GetCustomerDetailsImpl implements GetCustomerDetails {

  private final CustomerPort customerPort;

  private final ProfileImageURLProviderPort profileImageURLProviderPort;

  @Override
  public Customer execute(final UUID id) {
    log.debug("Getting customer details for id: {}", id);
    final Customer customer =
        customerPort
            .findById(id)
            .orElseThrow(() -> new CustomerNotFound("Customer not found for id: " + id));

    if (!customer.active()) {
      log.debug("Customer {} is not active", id);
      throw new CustomerNotFound("Customer not found for id: " + id);
    }

    final var currentImageKey = customer.profileImageUrl();
    if (currentImageKey == null) {
      return customer;
    }

    log.debug("Generating temp url for image: {}", currentImageKey);
    String tempImageUrl = profileImageURLProviderPort.generateURL(currentImageKey);

    return customer.toBuilder().profileImageUrl(tempImageUrl).build();
  }
}
