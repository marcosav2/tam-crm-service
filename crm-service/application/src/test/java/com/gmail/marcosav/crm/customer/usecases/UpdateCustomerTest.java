package com.gmail.marcosav.crm.customer.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.customer.exceptions.CustomerNotFound;
import com.gmail.marcosav2010.crm.customer.ports.CustomerPort;
import com.gmail.marcosav2010.crm.customer.ports.ProfileImagePort;
import com.gmail.marcosav2010.crm.customer.usecases.UpdateCustomerImpl;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateCustomerTest {

  @Mock private CustomerPort customerPort;

  @Mock private ProfileImagePort profileImagePort;

  @InjectMocks private UpdateCustomerImpl updateCustomer;

  @Test
  void execute_notExisting_throwNotFound() {
    final var customer =
        Customer.builder().id(UUID.randomUUID()).name("name").surname("surname").build();
    when(customerPort.findById(customer.id())).thenReturn(Optional.empty());

    assertThrows(CustomerNotFound.class, () -> updateCustomer.execute(customer, null, "user"));

    verify(customerPort).findById(customer.id());
    verifyNoMoreInteractions(customerPort);
    verifyNoInteractions(profileImagePort);
  }

  @Test
  void execute_withImage_saveAndUpdate() {
    final var user = "user";
    final var customer =
        Customer.builder().id(UUID.randomUUID()).name("newName").surname("surname").build();

    final var previousImageKey = "img";
    final var existingCustomer =
        customer.toBuilder()
            .name("name")
            .surname("surname")
            .profileImageUrl(previousImageKey)
            .build();

    final var mockedIS = mock(InputStream.class);
    final var mockedImageKey = "newImg";

    final var updatedCustomer = customer.toBuilder().profileImageUrl(mockedImageKey).build();

    when(customerPort.findById(customer.id())).thenReturn(Optional.of(existingCustomer));
    when(profileImagePort.save(mockedIS)).thenReturn(mockedImageKey);
    when(customerPort.update(updatedCustomer, user))
        .thenReturn(updatedCustomer.toBuilder().build());

    final var result = updateCustomer.execute(customer, mockedIS, user);

    assertThat(result).isEqualTo(updatedCustomer);

    verify(customerPort).update(updatedCustomer, user);
    verify(profileImagePort).delete(previousImageKey);
    verify(profileImagePort).save(mockedIS);
    verifyNoMoreInteractions(profileImagePort);
  }

  @Test
  void execute_withImageAndNotPreviousOne_saveAndUpdate() {
    final var user = "user";
    final var customer =
        Customer.builder().id(UUID.randomUUID()).name("newName").surname("surname").build();

    final var existingCustomer = customer.toBuilder().name("name").surname("surname").build();

    final var mockedIS = mock(InputStream.class);
    final var mockedImageKey = "newImg";

    final var updatedCustomer = customer.toBuilder().profileImageUrl(mockedImageKey).build();

    when(customerPort.findById(customer.id())).thenReturn(Optional.of(existingCustomer));
    when(profileImagePort.save(mockedIS)).thenReturn(mockedImageKey);
    when(customerPort.update(updatedCustomer, user))
        .thenReturn(updatedCustomer.toBuilder().build());

    final var result = updateCustomer.execute(customer, mockedIS, user);

    assertThat(result).isEqualTo(updatedCustomer);

    verify(customerPort).update(updatedCustomer, user);
    verify(profileImagePort).save(mockedIS);
    verifyNoMoreInteractions(profileImagePort);
  }

  @Test
  void execute_withImageButUnexpectedSaveError_deleteNewImage() {
    final var user = "user";
    final var customer =
        Customer.builder().id(UUID.randomUUID()).name("newName").surname("surname").build();

    final var previousImageKey = "img";
    final var existingCustomer =
        customer.toBuilder()
            .name("name")
            .surname("surname")
            .profileImageUrl(previousImageKey)
            .build();

    final var mockedIS = mock(InputStream.class);
    final var mockedImageKey = "newImg";

    final var updatedCustomer = customer.toBuilder().profileImageUrl(mockedImageKey).build();

    when(customerPort.findById(customer.id())).thenReturn(Optional.of(existingCustomer));
    when(profileImagePort.save(mockedIS)).thenReturn(mockedImageKey);
    when(customerPort.update(updatedCustomer, user)).thenThrow(new RuntimeException());

    assertThrows(RuntimeException.class, () -> updateCustomer.execute(customer, mockedIS, user));

    verify(customerPort).update(updatedCustomer, user);
    verify(profileImagePort).delete(previousImageKey);
    verify(profileImagePort).save(mockedIS);
    verify(profileImagePort).delete(mockedImageKey);
    verifyNoMoreInteractions(profileImagePort);
  }

  @Test
  void execute_withoutUpdatingImageAndNotHavingIt_justUpdate() {
    final var user = "user";
    final var customer =
        Customer.builder().id(UUID.randomUUID()).name("newName").surname("surname").build();

    final var existingCustomer = customer.toBuilder().name("name").surname("surname").build();

    when(customerPort.findById(customer.id())).thenReturn(Optional.of(existingCustomer));
    when(customerPort.update(customer, user)).thenReturn(customer.toBuilder().build());

    final var result = updateCustomer.execute(customer, null, user);

    assertThat(result).isEqualTo(customer);

    verify(customerPort).update(customer, user);
    verifyNoInteractions(profileImagePort);
  }

  @Test
  void execute_withoutUpdatingImageAndHavingItAlready_justUpdate() {
    final var user = "user";
    final var customer =
        Customer.builder().id(UUID.randomUUID()).name("newName").surname("surname").build();

    final var existingCustomer =
        customer.toBuilder().name("name").surname("surname").profileImageUrl("img").build();

    final var updatedCustomer =
        customer.toBuilder().profileImageUrl(existingCustomer.profileImageUrl()).build();

    when(customerPort.findById(customer.id())).thenReturn(Optional.of(existingCustomer));
    when(customerPort.update(updatedCustomer, user))
        .thenReturn(updatedCustomer.toBuilder().build());

    final var result = updateCustomer.execute(customer, null, user);

    assertThat(result).isEqualTo(updatedCustomer);

    verify(customerPort).update(updatedCustomer, user);
    verifyNoInteractions(profileImagePort);
  }
}
