package com.gmail.marcosav2010.crm.customer.usecases;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.customer.ports.CustomerPort;
import com.gmail.marcosav2010.crm.customer.ports.ProfileImageStoragePort;
import com.gmail.marcosav2010.crm.customer.ports.ProfileImageURLProviderPort;
import com.gmail.marcosav2010.crm.shared.entities.UploadFile;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@CustomLog
@Component
@RequiredArgsConstructor
public class RegisterCustomerImpl implements RegisterCustomer {

  private final CustomerPort customerPort;

  private final ProfileImageStoragePort profileImageStoragePort;

  private final ProfileImageURLProviderPort profileImageURLProviderPort;

  @Override
  public Customer execute(
      final Customer customer, final UploadFile profileImage, final String user) {
    log.debug("Creating customer {}", customer);

    String imageKey = null;
    if (profileImage != null) {
      imageKey = profileImageStoragePort.save(profileImage);
    }

    final var customerToSave = customer.toBuilder().active(true).profileImageUrl(imageKey).build();

    try {
      final var newProfileImageUrl =
          profileImage != null ? profileImageURLProviderPort.generateURL(imageKey) : null;
      return customerPort.register(customerToSave, user).toBuilder()
          .profileImageUrl(newProfileImageUrl)
          .build();

    } catch (Exception e) {
      if (imageKey != null) {
        profileImageStoragePort.delete(imageKey);
      }
      throw e;
    }
  }
}
