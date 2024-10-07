package com.gmail.marcosav2010.crm.customer.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.customer.exceptions.CustomerNotFound;
import com.gmail.marcosav2010.crm.customer.ports.CustomerPort;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteCustomerTest {

  @Mock private CustomerPort customerPort;

  @InjectMocks private DeleteCustomerImpl deleteCustomer;

  @Test
  void execute_notExisting_throwNotFound() {
    final UUID id = UUID.randomUUID();
    when(customerPort.findById(id)).thenReturn(Optional.empty());

    assertThrows(CustomerNotFound.class, () -> deleteCustomer.execute(id, "user"));

    verify(customerPort).findById(id);
    verifyNoMoreInteractions(customerPort);
  }

  @Test
  void execute_existing_deactivate() {
    final UUID id = UUID.randomUUID();
    final var customer =
        Customer.builder().id(id).name("name").surname("surname").active(true).build();

    when(customerPort.findById(id)).thenReturn(Optional.of(customer));

    deleteCustomer.execute(id, "user");

    verify(customerPort).findById(id);

    final var deletedCustomer = customer.toBuilder().active(false).build();
    verify(customerPort).update(deletedCustomer, "user");
  }
}
