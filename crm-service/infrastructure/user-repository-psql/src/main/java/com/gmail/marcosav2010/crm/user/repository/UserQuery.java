package com.gmail.marcosav2010.crm.user.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserQuery {
  QUERY_INSERT_USER(
      """
            INSERT INTO users (
              id,
              username,
              password,
              name,
              surname,
              active,
              role
            ) VALUES (
              :id,
              :username,
              :password,
              :name,
              :surname,
              :active,
              :role
             )
          """),
  QUERY_UPDATE_USER(
      """
            UPDATE users SET
              password = :password,
              name = :name,
              surname = :surname,
              active = :active,
              role = :role
            WHERE id = :id
          """),
  QUERY_FIND_USER_BY_ID(
      """
            SELECT
              id,
              username,
              password,
              name,
              surname,
              active,
              role
            FROM users
            WHERE id = :id
          """),
  QUERY_FIND_USER_BY_USERNAME(
      """
            SELECT
              id,
              username,
              password,
              name,
              surname,
              active,
              role
            FROM users
            WHERE username = :username
          """),
  QUERY_FIND_BY_ACTIVE_USERS_WITH_LIMIT_OFFSET(
      """
            SELECT
              id,
              username,
              password,
              name,
              surname,
              active,
              role
            FROM users
            WHERE active = :active
            LIMIT :limit
            OFFSET :offset
          """),
  QUERY_COUNT_BY_ACTIVE_USERS(
      """
            SELECT COUNT(*)
            FROM users
            WHERE active = :active
          """);

  private final String sql;
}
