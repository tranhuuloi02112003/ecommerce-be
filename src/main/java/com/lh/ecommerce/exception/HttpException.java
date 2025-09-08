package com.lh.ecommerce.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class HttpException extends RuntimeException {
  private final HttpStatus httpStatus;
  private final String code;

  public HttpException(final HttpStatus httpStatus, final Error error, final Object... args) {
    super(String.format(error.getMessage(), args));
    this.code = error.getName();
    this.httpStatus = httpStatus;
  }
}
