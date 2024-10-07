package com.gmail.marcosav2010.crm.user.entities;

import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record User(UUID id, String username, String name, String surname, boolean active) {}
