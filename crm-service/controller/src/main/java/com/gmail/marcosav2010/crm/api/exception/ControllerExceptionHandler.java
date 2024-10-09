package com.gmail.marcosav2010.crm.api.exception;

import com.gmail.marcosav2010.crm.api.dto.ErrorResponseDTO;
import com.gmail.marcosav2010.crm.customer.exceptions.CustomerNotFound;
import com.gmail.marcosav2010.crm.shared.exception.DomainValidationException;
import com.gmail.marcosav2010.crm.user.exception.UserNotFound;
import com.gmail.marcosav2010.crm.user.exception.UsernameAlreadyUsed;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

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
    var method = exception.getParameter().getMethod();
    var field = (method == null) ? "" : (method.getName() + ".");

    var providedType =
        (exception.getValue() == null) ? null : exception.getValue().getClass().getName();

    var expectedType =
        (exception.getRequiredType() == null) ? null : exception.getRequiredType().getName();

    String message =
        String.format(
            "Invalid type for field %s%s with value %s, provided %s but %s was expected",
            field, exception.getPropertyName(), exception.getValue(), providedType, expectedType);

    return build(exception, HttpStatus.BAD_REQUEST, message);
  }

  @ExceptionHandler(MissingRequestValueException.class)
  public ResponseEntity<ErrorResponseDTO> badRequestMissingValue(
      final MissingRequestValueException exception) {
    return build(exception, HttpStatus.BAD_REQUEST);
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
