package com.gmail.marcosav2010.crm.user.entities;

import com.gmail.marcosav2010.crm.shared.validation.Validate;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record User(
    UUID id,
    String password,
    String username,
    String name,
    String surname,
    boolean active,
    UserRole role) {

  public void validatePassword() {
    Validate.length("password", password, 8, 50);
  }

  public static class UserBuilder {

    public UserBuilder username(String username) {
      Validate.length("username", username, 5, 24);
      Validate.alphanumericPlus("username", username);
      this.username = username;
      return this;
    }

    public UserBuilder name(String name) {
      Validate.length("name", name, 2, 20);
      Validate.alphanumericPlus("name", name);
      this.name = name;
      return this;
    }

    public UserBuilder surname(String surname) {
      Validate.length("surname", surname, 2, 30);
      Validate.alphanumericPlus("surname", surname);
      this.surname = surname;
      return this;
    }

    public UserBuilder role(UserRole role) {
      Validate.notNull("role", role);
      this.role = role;
      return this;
    }
  }
}
