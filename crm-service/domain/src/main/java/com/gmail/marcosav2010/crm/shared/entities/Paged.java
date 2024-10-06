package com.gmail.marcosav2010.crm.shared.entities;

import java.util.List;
import lombok.Builder;

@Builder
public record Paged<T>(List<T> data, long results) {}
