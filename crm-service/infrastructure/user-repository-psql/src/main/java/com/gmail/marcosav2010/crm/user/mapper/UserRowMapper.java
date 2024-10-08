package com.gmail.marcosav2010.crm.user.mapper;

import com.gmail.marcosav2010.crm.user.entities.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class UserRowMapper implements RowMapper<User> {

  @Override
  public User mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    return User.builder()
        .id(UUID.fromString(rs.getString("id")))
        .username(rs.getString("username"))
        .password(rs.getString("password"))
        .name(rs.getString("name"))
        .surname(rs.getString("surname"))
        .active(rs.getBoolean("active"))
        .build();
  }

  public MapSqlParameterSource mapParams(final User user) {
    return new MapSqlParameterSource()
        .addValue("id", user.id())
        .addValue("name", user.name())
        .addValue("username", user.username())
        .addValue("password", user.password())
        .addValue("surname", user.surname())
        .addValue("active", user.active());
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

  public MapSqlParameterSource mapUsername(String username) {
    return new MapSqlParameterSource().addValue("username", username);
  }
}
