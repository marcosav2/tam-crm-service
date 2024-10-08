package com.gmail.marcosav2010.crm.customer.adapter;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.customer.ports.CustomerPort;
import com.gmail.marcosav2010.crm.customer.repository.CustomerRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerAdapter implements CustomerPort {

  private final CustomerRepository repository;

  @Override
  public Optional<Customer> findById(UUID id) {
    return this.repository.findById(id);
  }

  @Override
  public List<Customer> findActive(int page, int size) {
    return this.repository.findByActiveWithLimitAndOffset(true, size, page * size);
  }

  @Override
  public long countActive() {
    return this.repository.countByActive(true);
  }

  @Override
  public Customer update(Customer customer, String user) {
    final var customerToUpdate =
        customer.toBuilder().updatedAt(OffsetDateTime.now()).updatedBy(user).build();
    repository.update(customerToUpdate);
    return customerToUpdate;
  }

  @Override
  public Customer register(Customer customer, String user) {
    final var customerToInsert =
        customer.toBuilder()
            .id(UUID.randomUUID())
            .createdAt(OffsetDateTime.now())
            .createdBy(user)
            .build();
    repository.insert(customerToInsert);
    return customerToInsert;
  }
}
