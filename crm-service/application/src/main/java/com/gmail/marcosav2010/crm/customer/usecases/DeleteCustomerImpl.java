package com.gmail.marcosav2010.crm.customer.usecases;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.customer.exceptions.CustomerNotFound;
import com.gmail.marcosav2010.crm.customer.ports.CustomerPort;
import java.util.UUID;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@CustomLog
@Component
@RequiredArgsConstructor
public class DeleteCustomerImpl implements DeleteCustomer {

  private final CustomerPort customerPort;

  @Override
  public void execute(final UUID id, final String user) {
    log.debug("Getting customer details for id: {}", id);
    final Customer customer =
        customerPort
            .findById(id)
            .orElseThrow(() -> new CustomerNotFound("Customer not found for id: " + id));

    log.debug("Deleting (deactivating) customer {}", id);
    final var deletedCustomer = customer.toBuilder().active(false).build();
    customerPort.update(deletedCustomer, user);
  }
}
