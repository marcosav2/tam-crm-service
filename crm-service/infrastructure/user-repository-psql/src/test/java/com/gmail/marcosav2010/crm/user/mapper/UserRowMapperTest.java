package com.gmail.marcosav2010.crm.user.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.gmail.marcosav2010.crm.user.entities.User;
import java.sql.ResultSet;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserRowMapperTest {

  private final UserRowMapper userRowMapper = new UserRowMapper();

  @Test
  void mapRow() throws Exception {
    final ResultSet rs = Mockito.mock(ResultSet.class);
    final UUID id = UUID.randomUUID();

    when(rs.getString("id")).thenReturn(id.toString());
    when(rs.getString("username")).thenReturn("username");
    when(rs.getString("password")).thenReturn("password");
    when(rs.getString("name")).thenReturn("name");
    when(rs.getString("surname")).thenReturn("surname");
    when(rs.getBoolean("active")).thenReturn(true);

    final var customer = userRowMapper.mapRow(rs, 1);

    assertNotNull(customer);
    assertEquals(id, customer.id());
    assertEquals("name", customer.name());
    assertEquals("surname", customer.surname());
    assertTrue(customer.active());
  }

  @Test
  void mapRow_nulls() throws Exception {
    final ResultSet rs = Mockito.mock(ResultSet.class);
    final UUID id = UUID.randomUUID();

    when(rs.getString("id")).thenReturn(id.toString());
    when(rs.getString("username")).thenReturn("username");
    when(rs.getString("password")).thenReturn(null);
    when(rs.getString("name")).thenReturn("name");
    when(rs.getString("surname")).thenReturn("surname");
    when(rs.getBoolean("active")).thenReturn(true);

    final var customer = userRowMapper.mapRow(rs, 1);

    assertNotNull(customer);
    assertEquals(id, customer.id());
    assertEquals("username", customer.username());
    assertNull(customer.password());
    assertEquals("name", customer.name());
    assertEquals("surname", customer.surname());
    assertTrue(customer.active());
  }

  @Test
  void mapParams() {
    final var id = UUID.randomUUID();
    final var now = OffsetDateTime.now();
    final var user =
        User.builder()
            .id(id)
            .username("username")
            .password("password")
            .name("name")
            .surname("surname")
            .active(true)
            .build();

    final var params = userRowMapper.mapParams(user);

    assertNotNull(params);
    assertEquals(id, params.getValue("id"));
    assertEquals("username", params.getValue("username"));
    assertEquals("password", params.getValue("password"));
    assertEquals("name", params.getValue("name"));
    assertEquals("surname", params.getValue("surname"));
    assertEquals(true, params.getValue("active"));
  }
}
