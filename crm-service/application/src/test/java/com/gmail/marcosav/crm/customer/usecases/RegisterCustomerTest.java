package com.gmail.marcosav.crm.customer.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.customer.ports.CustomerPort;
import com.gmail.marcosav2010.crm.customer.ports.ProfileImagePort;
import com.gmail.marcosav2010.crm.customer.usecases.RegisterCustomerImpl;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegisterCustomerTest {

  @Mock private CustomerPort customerPort;

  @Mock private ProfileImagePort profileImagePort;

  @InjectMocks private RegisterCustomerImpl registerCustomer;

  @Test
  void execute_withImage_saveAndRegister() {
    final var user = "user";
    final var customer = Customer.builder().name("name").surname("surname").build();

    final var mockedIS = mock(InputStream.class);
    final var mockedImageKey = "img";

    when(customerPort.register(any(), any())).thenReturn(customer.toBuilder().build());
    when(profileImagePort.save(mockedIS)).thenReturn(mockedImageKey);

    final var result = registerCustomer.execute(customer, mockedIS, user);

    final var savedCustomer = customer.toBuilder().profileImageUrl(mockedImageKey).build();
    assertThat(result).isEqualTo(customer);

    verify(customerPort).register(savedCustomer, user);
    verify(profileImagePort).save(mockedIS);
    verifyNoMoreInteractions(profileImagePort);
  }

  @Test
  void execute_withImageButUnexpectedSaveError_deleteImage() {
    final var user = "user";
    final var customer = Customer.builder().name("name").surname("surname").build();

    final var mockedIS = mock(InputStream.class);
    final var mockedImageKey = "img";

    when(customerPort.register(any(), any())).thenThrow(new RuntimeException());
    when(profileImagePort.save(mockedIS)).thenReturn(mockedImageKey);

    assertThrows(RuntimeException.class, () -> registerCustomer.execute(customer, mockedIS, user));

    final var savedCustomer = customer.toBuilder().profileImageUrl(mockedImageKey).build();

    verify(customerPort).register(savedCustomer, user);
    verify(profileImagePort).save(mockedIS);
    verify(profileImagePort).delete(mockedImageKey);
  }

  @Test
  void execute_withoutImage_justRegister() {
    final var user = "user";
    final var customer = Customer.builder().name("name").surname("surname").build();

    when(customerPort.register(customer, user)).thenReturn(customer.toBuilder().build());

    final var result = registerCustomer.execute(customer, null, user);

    assertThat(result).isEqualTo(customer);

    verify(customerPort).register(customer, user);
    verifyNoInteractions(profileImagePort);
  }
}
