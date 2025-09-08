package com.lh.ecommerce.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpException {

  public BadRequestException(final Error error, final Object... args) {
    super(HttpStatus.BAD_REQUEST, error, args);
  }
}
