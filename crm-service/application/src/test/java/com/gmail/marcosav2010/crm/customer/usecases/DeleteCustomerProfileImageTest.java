package com.gmail.marcosav2010.crm.customer.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.customer.exceptions.CustomerNotFound;
import com.gmail.marcosav2010.crm.customer.ports.CustomerPort;
import com.gmail.marcosav2010.crm.customer.ports.ProfileImagePort;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteCustomerProfileImageTest {

  @Mock private CustomerPort customerPort;

  @Mock private ProfileImagePort profileImagePort;

  @InjectMocks private DeleteCustomerProfileImageImpl deleteCustomerProfileImage;

  @Test
  void execute_notExisting_throwNotFound() {
    final UUID id = UUID.randomUUID();
    when(customerPort.findById(id)).thenReturn(Optional.empty());

    assertThrows(CustomerNotFound.class, () -> deleteCustomerProfileImage.execute(id, "user"));

    verify(customerPort).findById(id);
    verifyNoMoreInteractions(customerPort);
    verifyNoInteractions(profileImagePort);
  }

  @Test
  void execute_existingCustomerWithImage_deleteImageAndUnlink() {
    final UUID id = UUID.randomUUID();
    final String oldImageKey = "old";
    final var customer =
        Customer.builder()
            .id(id)
            .name("name")
            .surname("surname")
            .profileImageUrl(oldImageKey)
            .build();

    when(customerPort.findById(id)).thenReturn(Optional.of(customer));

    deleteCustomerProfileImage.execute(id, "user");

    verify(customerPort).findById(id);
    verify(profileImagePort).delete(oldImageKey);

    final var deletedCustomer = customer.toBuilder().profileImageUrl(null).build();
    verify(customerPort).update(deletedCustomer, "user");
  }

  @Test
  void execute_existingCustomerWithoutImage_ignore() {
    final UUID id = UUID.randomUUID();
    final var customer =
        Customer.builder().id(id).name("name").surname("surname").profileImageUrl(null).build();

    when(customerPort.findById(id)).thenReturn(Optional.of(customer));

    deleteCustomerProfileImage.execute(id, "user");

    verify(customerPort).findById(id);
    verifyNoMoreInteractions(customerPort);
    verifyNoInteractions(profileImagePort);
  }
}
