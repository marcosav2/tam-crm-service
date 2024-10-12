package com.gmail.marcosav2010.crm.api.exception;

import com.gmail.marcosav2010.crm.api.dto.ErrorResponseDTO;
import com.gmail.marcosav2010.crm.customer.exceptions.CustomerNotFound;
import com.gmail.marcosav2010.crm.shared.exception.DomainValidationException;
import com.gmail.marcosav2010.crm.user.exception.UserNotFound;
import com.gmail.marcosav2010.crm.user.exception.UsernameAlreadyUsed;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import lombok.CustomLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@CustomLog
@RestControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler({CustomerNotFound.class, UserNotFound.class})
  public ResponseEntity<ErrorResponseDTO> notFound(final RuntimeException exception) {
    return build(exception, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponseDTO> badRequestValidationException(
      final ConstraintViolationException exception) {
    return build(exception, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({DomainValidationException.class, UsernameAlreadyUsed.class})
  public ResponseEntity<ErrorResponseDTO> domainValidationException(
      final RuntimeException exception) {
    return build(exception, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({InvalidImageTypeException.class})
  public ResponseEntity<ErrorResponseDTO> invalidFileFormatException(
      final InvalidImageTypeException exception) {
    return build(exception, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  @ExceptionHandler({MaxUploadSizeExceededException.class})
  public ResponseEntity<ErrorResponseDTO> maxUploadSizeExceeded(
      final MaxUploadSizeExceededException exception) {
    String message = "File size exceeds the maximum allowed size of 15MB";

    return build(exception, HttpStatus.PAYLOAD_TOO_LARGE, message);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponseDTO> badRequestTypeMismatch(
      final MethodArgumentTypeMismatchException exception) {
    final var method = exception.getParameter().getMethod();
    final var field = (method == null) ? "" : (method.getName() + ".");

    final var providedType =
        (exception.getValue() == null) ? null : exception.getValue().getClass().getName();

    final var expectedType =
        (exception.getRequiredType() == null) ? null : exception.getRequiredType().getName();

    final String message =
        String.format(
            "Invalid type for field %s%s with value %s, provided %s but %s was expected",
            field, exception.getPropertyName(), exception.getValue(), providedType, expectedType);

    return build(exception, HttpStatus.BAD_REQUEST, message);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDTO> badRequestTypeMismatch(
      final MethodArgumentNotValidException exception) {
    final var errors =
        exception.getBindingResult().getFieldErrors().stream()
            .map(
                fieldError ->
                    String.format(
                        "Field %s %s", fieldError.getField(), fieldError.getDefaultMessage()))
            .collect(Collectors.joining(", "));

    return build(exception, HttpStatus.BAD_REQUEST, errors);
  }

  @ExceptionHandler(MissingRequestValueException.class)
  public ResponseEntity<ErrorResponseDTO> badRequestMissingValue(
      final MissingRequestValueException exception) {
    return build(exception, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({
    BadCredentialsException.class,
    SignatureException.class,
    ExpiredJwtException.class,
    UsernameNotFoundException.class,
    InsufficientAuthenticationException.class
  })
  public ResponseEntity<ErrorResponseDTO> badCredentials(final Exception exception) {
    return build(exception, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponseDTO> accessDenied(final Exception exception) {
    return build(exception, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponseDTO> genericBadRequest(
      final HttpMessageNotReadableException exception) {
    return build(exception, HttpStatus.BAD_REQUEST, exception.getMostSpecificCause().getMessage());
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponseDTO> genericException(final RuntimeException exception) {
    log.error("Unexpected error", exception);
    return build(
        exception,
        HttpStatus.INTERNAL_SERVER_ERROR,
        exception.getClass().getSimpleName() + " - " + exception.getMessage());
  }

  private ResponseEntity<ErrorResponseDTO> build(
      final Throwable throwable, final HttpStatus status) {
    return build(throwable, status, null);
  }

  private ResponseEntity<ErrorResponseDTO> build(
      final Throwable throwable, final HttpStatus status, final String message) {
    var response = new ErrorResponseDTO(message == null ? throwable.getMessage() : message);
    return ResponseEntity.status(status).body(response);
  }
}
