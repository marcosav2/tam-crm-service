package com.gmail.marcosav2010.crm.customer.repository;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.customer.mapper.CustomerRowMapper;
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
public class CustomerRepository {

  private final CustomerRowMapper customerRowMapper;

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public void insert(final Customer customer) {
    final var params = this.customerRowMapper.mapParams(customer);
    log.debug("Executing customer insert with params: {}", params);
    this.jdbcTemplate.update(CustomerQuery.QUERY_INSERT_CUSTOMER.getSql(), params);
  }

  public void update(final Customer customer) {
    final var params = this.customerRowMapper.mapParams(customer);
    log.debug("Executing customer update with params: {}", params);
    this.jdbcTemplate.update(CustomerQuery.QUERY_UPDATE_CUSTOMER.getSql(), params);
  }

  public Optional<Customer> findById(final UUID id) {
    final var params = this.customerRowMapper.mapId(id);
    log.debug("Executing find customer by id with params: {}", params);
    try {
      return Optional.ofNullable(
          this.jdbcTemplate.queryForObject(
              CustomerQuery.QUERY_FIND_CUSTOMER_BY_ID.getSql(), params, this.customerRowMapper));
    } catch (final EmptyResultDataAccessException e) {
      log.debug("Customer with id {} not found", id);
      return Optional.empty();
    }
  }

  public List<Customer> findByActiveWithLimitAndOffset(boolean active, int limit, int offset) {
    final var params = this.customerRowMapper.mapActiveWithLimitAndOffset(active, limit, offset);
    log.debug("Executing find active customers with params {}", params);
    return this.jdbcTemplate.query(
        CustomerQuery.QUERY_FIND_BY_ACTIVE_CUSTOMERS_ORDERED_BY_CREATION_WITH_LIMIT_OFFSET.getSql(),
        params,
        this.customerRowMapper);
  }

  public long countByActive(boolean active) {
    final var params = this.customerRowMapper.mapActive(active);
    log.debug("Executing count active customers with params {}", params);
    final var result =
        this.jdbcTemplate.queryForObject(
            CustomerQuery.QUERY_COUNT_BY_ACTIVE_CUSTOMERS.getSql(), params, Long.class);
    log.debug("Count active customers result: {}", result);
    return result;
  }
}
