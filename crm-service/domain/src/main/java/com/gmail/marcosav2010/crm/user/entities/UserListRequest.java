package com.gmail.marcosav2010.crm.user.entities;

import com.gmail.marcosav2010.crm.shared.entities.Page;
import lombok.Builder;

@Builder
public record UserListRequest(Page page) {}
