package com.gmail.marcosav2010.crm.customer.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.customer.ports.CustomerPort;
import com.gmail.marcosav2010.crm.customer.ports.ProfileImageStoragePort;
import com.gmail.marcosav2010.crm.shared.entities.UploadFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegisterCustomerTest {

  @Mock private CustomerPort customerPort;

  @Mock private ProfileImageStoragePort profileImageStoragePort;

  @InjectMocks private RegisterCustomerImpl registerCustomer;

  @Test
  void execute_withImage_saveAndRegister() {
    final var user = "user";
    final var customer = Customer.builder().name("name").surname("surname").build();

    final var mockedFile = mock(UploadFile.class);
    final var mockedImageKey = "img";

    when(customerPort.register(any(), any())).thenReturn(customer.toBuilder().build());
    when(profileImageStoragePort.save(mockedFile)).thenReturn(mockedImageKey);

    final var result = registerCustomer.execute(customer, mockedFile, user);

    final var savedCustomer =
        customer.toBuilder().active(true).profileImageUrl(mockedImageKey).build();
    assertThat(result).isEqualTo(customer);

    verify(customerPort).register(savedCustomer, user);
    verify(profileImageStoragePort).save(mockedFile);
    verifyNoMoreInteractions(profileImageStoragePort);
  }

  @Test
  void execute_withImageButUnexpectedSaveError_deleteImage() {
    final var user = "user";
    final var customer = Customer.builder().name("name").surname("surname").build();

    final var mockedFile = mock(UploadFile.class);
    final var mockedImageKey = "img";

    when(customerPort.register(any(), any())).thenThrow(new RuntimeException());
    when(profileImageStoragePort.save(mockedFile)).thenReturn(mockedImageKey);

    assertThrows(
        RuntimeException.class, () -> registerCustomer.execute(customer, mockedFile, user));

    final var savedCustomer =
        customer.toBuilder().active(true).profileImageUrl(mockedImageKey).build();

    verify(customerPort).register(savedCustomer, user);
    verify(profileImageStoragePort).save(mockedFile);
    verify(profileImageStoragePort).delete(mockedImageKey);
  }

  @Test
  void execute_withoutImage_justRegister() {
    final var user = "user";
    final var customer = Customer.builder().name("name").surname("surname").build();

    when(customerPort.register(any(), eq(user))).thenReturn(customer.toBuilder().build());

    final var result = registerCustomer.execute(customer, null, user);

    assertThat(result).isEqualTo(customer);

    verify(customerPort).register(customer.toBuilder().active(true).build(), user);
    verifyNoInteractions(profileImageStoragePort);
  }
}
