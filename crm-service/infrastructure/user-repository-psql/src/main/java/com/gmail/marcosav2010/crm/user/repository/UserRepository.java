package com.gmail.marcosav2010.crm.user.repository;

import com.gmail.marcosav2010.crm.user.entities.User;
import com.gmail.marcosav2010.crm.user.mapper.UserRowMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@CustomLog
@Repository
@RequiredArgsConstructor
public class UserRepository {

  private final UserRowMapper userRowMapper;

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public void insert(final User user) {
    final var params = this.userRowMapper.mapParams(user);
    log.debug("Executing user insert with params: {}", params);
    this.jdbcTemplate.update(UserQuery.QUERY_INSERT_USER.getSql(), params);
  }

  public void update(final User user) {
    final var params = this.userRowMapper.mapParams(user);
    log.debug("Executing user update with params: {}", params);
    this.jdbcTemplate.update(UserQuery.QUERY_UPDATE_USER.getSql(), params);
  }

  public Optional<User> findById(final UUID id) {
    final var params = this.userRowMapper.mapId(id);
    log.debug("Executing find user by id with params: {}", params);
    try {
      return Optional.ofNullable(
          this.jdbcTemplate.queryForObject(
              UserQuery.QUERY_FIND_USER_BY_ID.getSql(), params, this.userRowMapper));
    } catch (final EmptyResultDataAccessException e) {
      log.debug("User with id {} not found", id);
      return Optional.empty();
    }
  }

  public Optional<User> findByUsername(String username) {
    final var params = this.userRowMapper.mapUsername(username);
    log.debug("Executing find user by username with params: {}", params);
    try {
      return Optional.ofNullable(
          this.jdbcTemplate.queryForObject(
              UserQuery.QUERY_FIND_USER_BY_USERNAME.getSql(), params, this.userRowMapper));
    } catch (final EmptyResultDataAccessException e) {
      log.debug("User with username {} not found", username);
      return Optional.empty();
    }
  }

  public List<User> findByActiveWithLimitAndOffset(boolean active, int limit, int offset) {
    final var params = this.userRowMapper.mapActiveWithLimitAndOffset(active, limit, offset);
    log.debug("Executing find active users with params {}", params);
    return this.jdbcTemplate.query(
        UserQuery.QUERY_FIND_BY_ACTIVE_USERS_WITH_LIMIT_OFFSET.getSql(),
        params,
        this.userRowMapper);
  }

  public long countByActive(boolean active) {
    final var params = this.userRowMapper.mapActive(active);
    log.debug("Executing count active users with params {}", params);
    final var result =
        this.jdbcTemplate.queryForObject(
            UserQuery.QUERY_COUNT_BY_ACTIVE_USERS.getSql(), params, Long.class);
    log.debug("Count active users result: {}", result);
    return result;
  }
}
