package com.gmail.marcosav2010.crm.customer.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomerQuery {
  QUERY_INSERT_CUSTOMER(
      """
            INSERT INTO customers (
              id,
              name,
              surname,
              profile_image_url,
              active,
              created_at,
              created_by,
              updated_at,
              updated_by
            ) VALUES (
              :id,
              :name,
              :surname,
              :profileImageUrl,
              :active,
              :createdAt,
              :createdBy,
              :updatedAt,
              :updatedBy
             )
          """),
  QUERY_UPDATE_CUSTOMER(
      """
            UPDATE customers SET
              name = :name,
              surname = :surname,
              profile_image_url = :profileImageUrl,
              active = :active,
              updated_at = :updatedAt,
              updated_by = :updatedBy
            WHERE id = :id
          """),
  QUERY_FIND_CUSTOMER_BY_ID(
      """
            SELECT
              id,
              name,
              surname,
              profile_image_url,
              active,
              created_at,
              created_by,
              updated_at,
              updated_by
            FROM customers
            WHERE id = :id
          """),
  QUERY_FIND_BY_ACTIVE_CUSTOMERS_ORDERED_BY_CREATION_WITH_LIMIT_OFFSET(
      """
            SELECT
              id,
              name,
              surname,
              profile_image_url,
              active,
              created_at,
              created_by,
              updated_at,
              updated_by
            FROM customers
            WHERE active = :active
            ORDER BY created_at DESC
            LIMIT :limit
            OFFSET :offset
          """),
  QUERY_COUNT_BY_ACTIVE_CUSTOMERS(
      """
            SELECT COUNT(*)
            FROM customers
            WHERE active = :active
          """);

  private final String sql;
}
