package com.lh.ecommerce.service.user;

import com.lh.ecommerce.exception.BadRequestException;
import com.lh.ecommerce.exception.Error;
import com.lh.ecommerce.exception.NotFoundException;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserError implements Error {
  USER_NOTFOUND("User not found"),
  EMAIL_ALREADY_EXISTS("Email already exists"),
  ;
  private final String message;

  @Override
  public String getName() {
    return name();
  }

  public static Supplier<NotFoundException> userNotFound() {
    return () -> new NotFoundException(USER_NOTFOUND);
  }

  public static Supplier<BadRequestException> emailAlreadyExists() {
    return () -> new BadRequestException(EMAIL_ALREADY_EXISTS);
  }
}
