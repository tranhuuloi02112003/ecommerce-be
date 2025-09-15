package com.lh.ecommerce.security;

import com.lh.ecommerce.exception.BadRequestException;
import com.lh.ecommerce.exception.Error;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SecurityError implements Error {
  INVALID_TOKEN("Invalid Token");
  private final String message;

  @Override
  public String getName() {
    return name();
  }

  public static Supplier<BadRequestException> invalidToken() {
    return () -> new BadRequestException(INVALID_TOKEN);
  }
}
