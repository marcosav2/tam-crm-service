package com.gmail.marcosav2010.crm.customer.entities;

import com.gmail.marcosav2010.crm.shared.validation.Validate;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record Customer(
    UUID id,
    String name,
    String surname,
    String profileImageUrl,
    OffsetDateTime createdAt,
    String createdBy,
    OffsetDateTime updatedAt,
    String updatedBy,
    boolean active) {

  public static class CustomerBuilder {

    public CustomerBuilder name(String name) {
      Validate.length("name", name, 2, 20);
      Validate.alphanumericPlus("name", name);
      this.name = name;
      return this;
    }

    public CustomerBuilder surname(String surname) {
      Validate.length("surname", surname, 2, 30);
      Validate.alphanumericPlus("surname", surname);
      this.surname = surname;
      return this;
    }
  }
}
