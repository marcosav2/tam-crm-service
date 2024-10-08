package com.gmail.marcosav2010.crm.customer.usecases;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.customer.exceptions.CustomerNotFound;
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
public class UpdateCustomerImpl implements UpdateCustomer {

  private final CustomerPort customerPort;

  private final ProfileImageStoragePort profileImageStoragePort;

  private final ProfileImageURLProviderPort profileImageURLProviderPort;

  @Override
  public Customer execute(
      final Customer customer, final UploadFile profileImage, final String user) {
    log.debug("Getting customer details for id: {}", customer.id());
    final Customer existingCustomer =
        customerPort
            .findById(customer.id())
            .orElseThrow(() -> new CustomerNotFound("Customer not found for id: " + customer.id()));

    final String imageKey = updateProfileImage(existingCustomer, profileImage);

    log.debug("Updating customer {}", customer);
    final var customerToUpdate =
        customer.toBuilder().active(existingCustomer.active()).profileImageUrl(imageKey).build();

    try {
      final var newProfileImageUrl =
          imageKey != null ? profileImageURLProviderPort.generateURL(imageKey) : null;
      return customerPort.update(customerToUpdate, user).toBuilder()
          .profileImageUrl(newProfileImageUrl)
          .build();

    } catch (Exception e) {
      if (profileImage != null) {
        log.error("Deleting profile image because of an unexpected error: {}", imageKey);
        profileImageStoragePort.delete(imageKey);
      }
      throw e;
    }
  }

  private String updateProfileImage(
      final Customer existingCustomer, final UploadFile profileImage) {
    String imageKey = existingCustomer.profileImageUrl();
    if (profileImage != null) {
      if (imageKey != null) {
        log.debug("Deleting existing profile image: {}", imageKey);
        profileImageStoragePort.delete(imageKey);
      }
      log.trace("Saving new profile image");
      imageKey = profileImageStoragePort.save(profileImage);
      log.debug("New profile image saved with key: {}", imageKey);
    }
    return imageKey;
  }
}
