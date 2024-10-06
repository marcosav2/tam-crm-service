package com.gmail.marcosav2010.crm.customer.entities;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record Customer(
    UUID id,
    String name,
    String surname,
    String profileImageUrl,
    OffsetDateTime createdAt,
    String createdBy,
    OffsetDateTime updatedAt,
    String updatedBy) {}
