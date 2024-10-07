package com.gmail.marcosav2010.crm.customer.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.customer.entities.CustomerListRequest;
import com.gmail.marcosav2010.crm.customer.ports.CustomerPort;
import com.gmail.marcosav2010.crm.customer.ports.ProfileImagePort;
import com.gmail.marcosav2010.crm.shared.entities.Page;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListActiveCustomersTest {

  @Mock private CustomerPort customerPort;

  @Mock private ProfileImagePort profileImagePort;

  @InjectMocks private ListActiveCustomersImpl listActiveCustomers;

  @Test
  void execute_hugePageSize_illegalArg() {
    final var request =
        CustomerListRequest.builder().page(Page.builder().page(1).size(1000).build()).build();

    assertThrows(IllegalArgumentException.class, () -> listActiveCustomers.execute(request));

    verifyNoInteractions(customerPort);
    verifyNoInteractions(profileImagePort);
  }

  @Test
  void execute_withPhoto_generatePhotoAndReturn() {
    final var request =
        CustomerListRequest.builder().page(Page.builder().page(1).size(10).build()).build();

    final var customer =
        Customer.builder()
            .id(UUID.randomUUID())
            .name("name")
            .surname("surname")
            .profileImageUrl("img")
            .build();

    when(customerPort.findActive(1, 10)).thenReturn(List.of(customer));
    when(customerPort.countActive()).thenReturn(1L);

    final var mockedTempUrl = "tempUrl";
    when(profileImagePort.generateTempUrl(customer.profileImageUrl())).thenReturn(mockedTempUrl);

    final var results = listActiveCustomers.execute(request);

    assertThat(results)
        .satisfies(
            r -> {
              assertThat(r.results()).isEqualTo(1);
              assertThat(r.data())
                  .containsExactly(customer.toBuilder().profileImageUrl(mockedTempUrl).build());
            });

    verify(customerPort).findActive(1, 10);
    verify(customerPort).countActive();
    verify(profileImagePort).generateTempUrl(customer.profileImageUrl());
  }

  @Test
  void execute_existingWithoutPhoto_return() {
    final var request =
        CustomerListRequest.builder().page(Page.builder().page(1).size(10).build()).build();

    final var customer =
        Customer.builder()
            .id(UUID.randomUUID())
            .name("name")
            .surname("surname")
            .profileImageUrl(null)
            .build();

    when(customerPort.findActive(1, 10)).thenReturn(List.of(customer));
    when(customerPort.countActive()).thenReturn(1L);

    final var results = listActiveCustomers.execute(request);

    assertThat(results)
        .satisfies(
            r -> {
              assertThat(r.results()).isEqualTo(1);
              assertThat(r.data()).containsExactly(customer);
            });

    verify(customerPort).findActive(1, 10);
    verify(customerPort).countActive();
    verifyNoInteractions(profileImagePort);
  }
}
