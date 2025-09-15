package com.lh.ecommerce.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends HttpException {

  public ForbiddenException(final Error error, final Object... args) {
    super(HttpStatus.FORBIDDEN, error, args);
  }
}
