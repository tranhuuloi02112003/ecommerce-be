package com.lh.ecommerce.exception;

import com.lh.ecommerce.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
    return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentialsException(
      BadCredentialsException ex, HttpServletRequest request) {
    return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED, request);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentialsException(
      UsernameNotFoundException ex, HttpServletRequest request) {
    return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED, request);
  }

  @ExceptionHandler(AppException.class)
  public ResponseEntity<ErrorResponse> handleAppException(
      AppException ex, HttpServletRequest request) {
    return buildErrorResponse(ex, ex.getStatus(), request);
  }

  private ResponseEntity<ErrorResponse> buildErrorResponse(
      Exception ex, HttpStatus status, HttpServletRequest request) {
    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .status(status.value())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .timestamp(LocalDateTime.now())
            .build();

    return new ResponseEntity<>(errorResponse, status);
  }
}
