package com.lh.ecommerce.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends HttpException {

  public UnauthorizedException(final Error error, final Object... args) {
    super(HttpStatus.UNAUTHORIZED, error, args);
  }
}
