package com.gmail.marcosav2010.crm.customer.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import java.sql.ResultSet;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CustomerRowMapperTest {

  private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

  @Test
  void mapRow() throws Exception {
    final ResultSet rs = Mockito.mock(ResultSet.class);
    final UUID id = UUID.randomUUID();
    final var now = OffsetDateTime.now();

    when(rs.getString("id")).thenReturn(id.toString());
    when(rs.getString("name")).thenReturn("name");
    when(rs.getString("surname")).thenReturn("surname");
    when(rs.getString("profile_image_url")).thenReturn("profile_image_url");
    when(rs.getBoolean("active")).thenReturn(true);
    when(rs.getObject("created_at", OffsetDateTime.class)).thenReturn(now);
    when(rs.getString("created_by")).thenReturn("created_by");
    when(rs.getObject("updated_at", OffsetDateTime.class)).thenReturn(now);
    when(rs.getString("updated_by")).thenReturn("updated_by");

    final var customer = customerRowMapper.mapRow(rs, 1);

    assertNotNull(customer);
    assertEquals(id, customer.id());
    assertEquals("name", customer.name());
    assertEquals("surname", customer.surname());
    assertEquals("profile_image_url", customer.profileImageUrl());
    assertTrue(customer.active());
    assertEquals(now, customer.createdAt());
    assertEquals("created_by", customer.createdBy());
    assertEquals(now, customer.updatedAt());
    assertEquals("updated_by", customer.updatedBy());
  }

  @Test
  void mapRow_nulls() throws Exception {
    final ResultSet rs = Mockito.mock(ResultSet.class);
    final UUID id = UUID.randomUUID();
    final var now = OffsetDateTime.now();

    when(rs.getString("id")).thenReturn(id.toString());
    when(rs.getString("name")).thenReturn("name");
    when(rs.getString("surname")).thenReturn("surname");
    when(rs.getString("profile_image_url")).thenReturn(null);
    when(rs.getBoolean("active")).thenReturn(true);
    when(rs.getObject("created_at", OffsetDateTime.class)).thenReturn(now);
    when(rs.getString("created_by")).thenReturn("created_by");
    when(rs.getObject("updated_at", OffsetDateTime.class)).thenReturn(null);
    when(rs.getString("updated_by")).thenReturn(null);

    final var customer = customerRowMapper.mapRow(rs, 1);

    assertNotNull(customer);
    assertEquals(id, customer.id());
    assertEquals("name", customer.name());
    assertEquals("surname", customer.surname());
    assertNull(customer.profileImageUrl());
    assertTrue(customer.active());
    assertEquals(now, customer.createdAt());
    assertEquals("created_by", customer.createdBy());
    assertNull(customer.updatedAt());
    assertNull(customer.updatedBy());
  }

  @Test
  void mapParams() {
    final var id = UUID.randomUUID();
    final var now = OffsetDateTime.now();
    final var customer =
        Customer.builder()
            .id(id)
            .name("name")
            .surname("surname")
            .profileImageUrl("profile_image_url")
            .active(true)
            .createdAt(now)
            .createdBy("created_by")
            .updatedAt(now)
            .updatedBy("updated_by")
            .build();

    final var params = customerRowMapper.mapParams(customer);

    assertNotNull(params);
    assertEquals(id, params.getValue("id"));
    assertEquals("name", params.getValue("name"));
    assertEquals("surname", params.getValue("surname"));
    assertEquals("profile_image_url", params.getValue("profileImageUrl"));
    assertEquals(true, params.getValue("active"));
    assertEquals(now, params.getValue("createdAt"));
    assertEquals("created_by", params.getValue("createdBy"));
    assertEquals(now, params.getValue("updatedAt"));
    assertEquals("updated_by", params.getValue("updatedBy"));
  }
}
