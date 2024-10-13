package com.gmail.marcosav2010.crm.customer.mapper;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class CustomerRowMapper implements RowMapper<Customer> {

  @Override
  public Customer mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    return Customer.builder()
        .id(UUID.fromString(rs.getString("id")))
        .name(rs.getString("name"))
        .surname(rs.getString("surname"))
        .profileImageUrl(rs.getString("profile_image_url"))
        .active(rs.getBoolean("active"))
        .createdAt(rs.getObject("created_at", OffsetDateTime.class))
        .createdBy(rs.getString("created_by"))
        .updatedAt(rs.getObject("updated_at", OffsetDateTime.class))
        .updatedBy(rs.getString("updated_by"))
        .build();
  }

  public MapSqlParameterSource mapParams(final Customer customer) {
    return new MapSqlParameterSource()
        .addValue("id", customer.id())
        .addValue("name", customer.name())
        .addValue("surname", customer.surname())
        .addValue("profileImageUrl", customer.profileImageUrl())
        .addValue("active", customer.active())
        .addValue("createdAt", customer.createdAt())
        .addValue("createdBy", customer.createdBy())
        .addValue("updatedAt", customer.updatedAt())
        .addValue("updatedBy", customer.updatedBy());
  }

  public MapSqlParameterSource mapId(final UUID id) {
    return new MapSqlParameterSource().addValue("id", id);
  }

  public MapSqlParameterSource mapActiveWithLimitAndOffset(boolean active, int limit, int offset) {
    return new MapSqlParameterSource()
        .addValue("active", active)
        .addValue("limit", limit)
        .addValue("offset", offset);
  }

  public MapSqlParameterSource mapActive(boolean active) {
    return new MapSqlParameterSource().addValue("active", active);
  }
}
