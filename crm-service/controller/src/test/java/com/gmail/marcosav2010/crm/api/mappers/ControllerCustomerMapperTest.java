package com.gmail.marcosav2010.crm.api.mappers;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.shared.entities.Paged;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ControllerCustomerMapperTest {

  private final ControllerCustomerMapper controllerCustomerMapper =
      Mappers.getMapper(ControllerCustomerMapper.class);

  @Test
  void map() {
    final var createdAt = OffsetDateTime.now();
    final var updatedAt = OffsetDateTime.now().plusDays(1);
    final var customer =
        Customer.builder()
            .id(UUID.randomUUID())
            .name("name")
            .surname("surname")
            .profileImageUrl("profileImageUrl")
            .active(true)
            .createdAt(createdAt)
            .createdBy("createdBy")
            .updatedAt(updatedAt)
            .updatedBy("updatedBy")
            .build();

    final var customerDTO = controllerCustomerMapper.map(customer);

    Assertions.assertEquals(customer.id(), customerDTO.getId());
    Assertions.assertEquals(customer.name(), customerDTO.getName());
    Assertions.assertEquals(customer.surname(), customerDTO.getSurname());
    Assertions.assertEquals(customer.profileImageUrl(), customerDTO.getProfileImageUrl());
    Assertions.assertEquals(customer.createdAt(), customerDTO.getCreatedAt());
    Assertions.assertEquals(customer.createdBy(), customerDTO.getCreatedBy());
    Assertions.assertEquals(customer.updatedAt(), customerDTO.getUpdatedAt());
    Assertions.assertEquals(customer.updatedBy(), customerDTO.getUpdatedBy());
  }

  @Test
  void mapOverview() {
    final var customer =
        Customer.builder()
            .id(UUID.randomUUID())
            .name("name")
            .surname("surname")
            .profileImageUrl("profileImageUrl")
            .active(true)
            .createdAt(OffsetDateTime.now())
            .createdBy("createdBy")
            .updatedAt(OffsetDateTime.now().plusDays(1))
            .updatedBy("updatedBy")
            .build();

    final var customerOverviewDTO = controllerCustomerMapper.mapOverview(customer);

    Assertions.assertEquals(customer.id(), customerOverviewDTO.getId());
    Assertions.assertEquals(customer.name(), customerOverviewDTO.getName());
    Assertions.assertEquals(customer.surname(), customerOverviewDTO.getSurname());
    Assertions.assertEquals(customer.profileImageUrl(), customerOverviewDTO.getProfileImageUrl());
  }

  @Test
  void mapPaged() {
    final var customer =
        Customer.builder()
            .id(UUID.randomUUID())
            .name("name")
            .surname("surname")
            .profileImageUrl("profileImageUrl")
            .active(true)
            .createdAt(OffsetDateTime.now())
            .createdBy("createdBy")
            .updatedAt(OffsetDateTime.now().plusDays(1))
            .updatedBy("updatedBy")
            .build();

    final var paged = new Paged<>(List.of(customer), 1);

    final var dto = controllerCustomerMapper.map(paged);

    Assertions.assertEquals(1, dto.getResults());
    Assertions.assertEquals(1, dto.getData().size());
    Assertions.assertEquals(customer.id(), dto.getData().getFirst().getId());
    Assertions.assertEquals(customer.name(), dto.getData().getFirst().getName());
    Assertions.assertEquals(customer.surname(), dto.getData().getFirst().getSurname());
    Assertions.assertEquals(
        customer.profileImageUrl(), dto.getData().getFirst().getProfileImageUrl());
  }
}
