package com.gmail.marcosav2010.crm.customer.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.customer.repository.CustomerRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerAdapterTest {

  @Mock private CustomerRepository repository;

  @InjectMocks private CustomerAdapter adapter;

  @Test
  void findById_matches_notEmpty() {
    final Customer customer =
        Customer.builder().id(UUID.randomUUID()).name("name").surname("surname").build();

    when(this.repository.findById(customer.id())).thenReturn(Optional.of(customer));

    final var optionalInc = this.adapter.findById(customer.id());

    assertThat(optionalInc).isNotEmpty().contains(customer);

    verify(this.repository).findById(customer.id());
  }

  @Test
  void findById_noMatches_empty() {
    when(this.repository.findById(any())).thenReturn(Optional.empty());

    final var optionalInc = this.adapter.findById(UUID.randomUUID());

    assertThat(optionalInc).isEmpty();
  }

  @Test
  void findActive_matches() {
    final var customers =
        List.of(
            Customer.builder().id(UUID.randomUUID()).name("name").surname("surname").build(),
            Customer.builder().id(UUID.randomUUID()).name("name").surname("surname").build());

    when(this.repository.findByActiveWithLimitAndOffset(true, 10, 0)).thenReturn(customers);

    final var result = this.adapter.findActive(0, 10);

    assertThat(result).isNotEmpty().containsExactlyElementsOf(customers);

    verify(this.repository).findByActiveWithLimitAndOffset(true, 10, 0);
  }

  @Test
  void countActive_matches() {
    when(this.repository.countByActive(true)).thenReturn(10L);

    final var result = this.adapter.countActive();

    assertThat(result).isEqualTo(10L);

    verify(this.repository).countByActive(true);
  }

  @Test
  void update() {
    final Customer customer =
        Customer.builder().id(UUID.randomUUID()).name("name").surname("surname").build();

    final var updated = this.adapter.update(customer, "user");

    assertThat(updated).isNotNull();
    assertThat(updated.updatedBy()).isEqualTo("user");
    assertThat(updated.updatedAt()).isNotNull();

    verify(this.repository).update(updated);
  }

  @Test
  void register() {
    final Customer customer = Customer.builder().name("name").surname("surname").build();

    final var registered = this.adapter.register(customer, "user");

    assertThat(registered).isNotNull();
    assertThat(registered.id()).isNotNull();
    assertThat(registered.createdAt()).isNotNull();
    assertThat(registered.createdBy()).isEqualTo("user");

    verify(this.repository).insert(registered);
  }
}
