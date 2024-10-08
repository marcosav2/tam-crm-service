package com.gmail.marcosav2010.crm.customer.usecases;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.customer.entities.CustomerListRequest;
import com.gmail.marcosav2010.crm.customer.ports.CustomerPort;
import com.gmail.marcosav2010.crm.customer.ports.ProfileImagePort;
import com.gmail.marcosav2010.crm.shared.constants.PageConstants;
import com.gmail.marcosav2010.crm.shared.entities.Page;
import com.gmail.marcosav2010.crm.shared.entities.Paged;
import java.util.List;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@CustomLog
@Component
@RequiredArgsConstructor
public class ListActiveCustomersImpl implements ListActiveCustomers {

  private final CustomerPort customerPort;

  private final ProfileImagePort profileImagePort;

  @Override
  public Paged<Customer> execute(final CustomerListRequest request) {
    log.debug("Listing customers for {}", request);

    if (request.page().size() > PageConstants.MAX_PAGE_SIZE) {
      throw new IllegalArgumentException(
          "Page size exceeds maximum allowed: " + PageConstants.MAX_PAGE_SIZE);
    }

    final Page requestedPage = request.page();
    final List<Customer> customers =
        customerPort.findActive(requestedPage.page() - 1, requestedPage.size()).stream()
            .map(c -> c.toBuilder().profileImageUrl(generateCustomerImageUrl(c)).build())
            .toList();

    final long results = customerPort.countActive();

    return Paged.<Customer>builder().data(customers).results(results).build();
  }

  private String generateCustomerImageUrl(Customer customer) {
    final var currentImageKey = customer.profileImageUrl();
    if (currentImageKey == null) {
      return null;
    }

    log.debug("Generating temp url for image: {}", currentImageKey);
    return profileImagePort.generateTempUrl(currentImageKey);
  }
}
