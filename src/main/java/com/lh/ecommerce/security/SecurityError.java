package com.lh.ecommerce.security;

import com.lh.ecommerce.exception.Error;
import com.lh.ecommerce.exception.UnauthorizedException;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SecurityError implements Error {
  INVALID_TOKEN("Invalid Token"),
  EXPIRED_TOKEN("Expired Token"),
  TOKEN_NOT_FOUND("Token not found"),
  INVALID_TOKEN_TYPE("Invalid token type"),
  REFRESH_TOKEN_JTI_INVALID("Invalid refresh token id"),
  EMAIL_NOT_FOUND("Email not found"),
  REFRESH_TOKEN_ROTATED("Refresh token is invalid or rotated"),
  LOGOUT_TOKEN("Token has been logged out"),
  ;

  private final String message;

  @Override
  public String getName() {
    return name();
  }

  public static Supplier<UnauthorizedException> invalidToken() {
    return () -> new UnauthorizedException(INVALID_TOKEN);
  }

  public static Supplier<UnauthorizedException> expiredToken() {
    return () -> new UnauthorizedException(EXPIRED_TOKEN);
  }

  public static Supplier<UnauthorizedException> tokenNotFound() {
    return () -> new UnauthorizedException(TOKEN_NOT_FOUND);
  }

  public static Supplier<UnauthorizedException> invalidTokenType() {
    return () -> new UnauthorizedException(INVALID_TOKEN_TYPE);
  }

  public static Supplier<UnauthorizedException> refreshTokenJtiInvalid() {
    return () -> new UnauthorizedException(REFRESH_TOKEN_JTI_INVALID);
  }

  public static Supplier<UnauthorizedException> emailNotFound() {
    return () -> new UnauthorizedException(EMAIL_NOT_FOUND);
  }

  public static Supplier<UnauthorizedException> refreshTokenRotated() {
    return () -> new UnauthorizedException(REFRESH_TOKEN_ROTATED);
  }

  public static Supplier<UnauthorizedException> logoutToken() {
    return () -> new UnauthorizedException(LOGOUT_TOKEN);
  }
}
