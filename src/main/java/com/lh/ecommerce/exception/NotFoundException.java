package com.lh.ecommerce.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends HttpException {

  public NotFoundException(final Error error, final Object... args) {
    super(HttpStatus.NOT_FOUND, error, args);
  }
}
