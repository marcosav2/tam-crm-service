package com.gmail.marcosav2010.crm.customer.usecases;

import static org.assertj.core.api.Assertions.assertThat;
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
class GetCustomerDetailsTest {

  @Mock private CustomerPort customerPort;

  @Mock private ProfileImagePort profileImagePort;

  @InjectMocks private GetCustomerDetailsImpl getCustomerDetails;

  @Test
  void execute_notExisting_throwNotFound() {
    final UUID id = UUID.randomUUID();
    when(customerPort.findById(id)).thenReturn(Optional.empty());

    assertThrows(CustomerNotFound.class, () -> getCustomerDetails.execute(id));

    verify(customerPort).findById(id);
    verifyNoInteractions(profileImagePort);
  }

  @Test
  void execute_existingDeactivated_throwNotFound() {
    final UUID id = UUID.randomUUID();
    final var customer =
        Customer.builder()
            .id(id)
            .name("name")
            .surname("surname")
            .active(false)
            .profileImageUrl(null)
            .build();

    when(customerPort.findById(id)).thenReturn(Optional.of(customer));

    assertThrows(CustomerNotFound.class, () -> getCustomerDetails.execute(id));

    verify(customerPort).findById(id);
    verifyNoInteractions(profileImagePort);
  }

  @Test
  void execute_existingWithPhoto_generatePhotoAndReturn() {
    final UUID id = UUID.randomUUID();

    final var customer =
        Customer.builder()
            .id(id)
            .name("name")
            .surname("surname")
            .active(true)
            .profileImageUrl("img")
            .build();

    when(customerPort.findById(id)).thenReturn(Optional.of(customer));

    final var mockedTempUrl = "tempUrl";
    when(profileImagePort.generateTempUrl(customer.profileImageUrl())).thenReturn(mockedTempUrl);

    final var result = getCustomerDetails.execute(id);

    assertThat(result).isEqualTo(customer.toBuilder().profileImageUrl(mockedTempUrl).build());

    verify(customerPort).findById(id);
    verify(profileImagePort).generateTempUrl(customer.profileImageUrl());
  }

  @Test
  void execute_existingWithoutPhoto_return() {
    final UUID id = UUID.randomUUID();

    final var customer =
        Customer.builder()
            .id(id)
            .name("name")
            .surname("surname")
            .active(true)
            .profileImageUrl(null)
            .build();

    when(customerPort.findById(id)).thenReturn(Optional.of(customer));

    final var result = getCustomerDetails.execute(id);

    assertThat(result).isEqualTo(customer);

    verify(customerPort).findById(id);
    verifyNoInteractions(profileImagePort);
  }
}
