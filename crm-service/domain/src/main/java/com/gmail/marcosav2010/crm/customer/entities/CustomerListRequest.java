package com.gmail.marcosav2010.crm.customer.entities;

import com.gmail.marcosav2010.crm.shared.entities.Page;
import lombok.Builder;

@Builder
public record CustomerListRequest(Page page) {}
