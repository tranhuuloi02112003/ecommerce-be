package com.lh.ecommerce.controller;

import static org.springframework.http.HttpStatus.*;

import com.lh.ecommerce.exception.HttpException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerHandler {

  @Getter
  @Builder
  public static class ErrorDto {
    private Instant timestamp;
    private int status;
    private String code;
    private String message;
    private String method;
    private String path;
  }

  @ResponseBody
  @ExceptionHandler(HttpException.class)
  public ResponseEntity<ErrorDto> handleHttpException(
      final HttpServletRequest req, final HttpException ex) {
    return createResponseEntity(req, ex.getMessage(), ex.getHttpStatus(), ex);
  }

  @ResponseBody
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(
      final HttpServletRequest req, final MethodArgumentNotValidException ex) {
    final var fieldError = ex.getBindingResult().getFieldError();
    final var message =
        (fieldError != null)
            ? fieldError.getField() + " " + fieldError.getDefaultMessage()
            : ex.getMessage();

    return createResponseEntity(req, message, BAD_REQUEST, ex);
  }

  @ResponseBody
  @ExceptionHandler(HttpMessageConversionException.class)
  public ResponseEntity<ErrorDto> handleHttpMessageConversionException(
      final HttpServletRequest req, final HttpMessageConversionException ex) {
    return createResponseEntity(req, ex.getMessage(), BAD_REQUEST, ex);
  }

  @ResponseBody
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorDto> handleInternalException(
      final HttpServletRequest req, final AccessDeniedException ex) {
    return createResponseEntity(req, ex.getMessage(), FORBIDDEN, ex);
  }

  @ResponseBody
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handleInternalException(
      final HttpServletRequest req, final Exception ex) {
    return createResponseEntity(req, ex.getMessage(), INTERNAL_SERVER_ERROR, ex);
  }

  private static ResponseEntity<ErrorDto> createResponseEntity(
      final HttpServletRequest req,
      final String message,
      final HttpStatus httpStatus,
      final Exception exception) {
    return ResponseEntity.status(httpStatus)
        .body(
            ErrorDto.builder()
                .timestamp(Instant.now())
                .status(httpStatus.value())
                .code(httpStatus.name())
                .message(message)
                .path(req.getRequestURI())
                .method(req.getMethod())
                .build());
  }
}
