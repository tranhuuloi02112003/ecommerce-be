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
  INVALID_CURRENT_PASSWORD("Current password is incorrect"),
  INVALID_VERIFICATION_TOKEN("Invalid or expired verification token"),
  ACCOUNT_ALREADY_VERIFIED("Account is already verified"),
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

  public static Supplier<BadRequestException> invalidCurrentPassword() {
    return () -> new BadRequestException(INVALID_CURRENT_PASSWORD);
  }

  public static Supplier<NotFoundException> invalidVerificationToken() {
    return () -> new NotFoundException(INVALID_VERIFICATION_TOKEN);
  }

  public static Supplier<BadRequestException> accountAlreadyVerified() {
    return () -> new BadRequestException(ACCOUNT_ALREADY_VERIFIED);
  }
}
