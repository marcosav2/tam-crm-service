package com.gmail.marcosav2010.crm.shared.validation;

import com.gmail.marcosav2010.crm.shared.exception.DomainValidationException;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Validate {

  private static final Pattern ALPHANUMERIC_PLUS_PATTERN = Pattern.compile("[a-zA-Z0-9\\-_.]+");

  public static void length(final String field, final String value, final int min, final int max) {
    if (value == null || value.length() < min || value.length() > max) {
      throw new DomainValidationException(
          String.format("Field %s must be between %d and %d characters", field, min, max));
    }
  }

  public static void alphanumericPlus(final String field, final String value) {
    if (value == null || !ALPHANUMERIC_PLUS_PATTERN.matcher(value).matches()) {
      throw new DomainValidationException(
          String.format("Field %s must be alphanumeric or \"_\", \"-\", \".\"", field));
    }
  }
}
