package com.lh.ecommerce.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieAdapter {
  private static final String REFRESH_TOKEN_NAME = "refreshToken";

  @Value("${spring.security.refresh-minutes}")
  private int refreshTokenMaxAge;

  public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
    ResponseCookie cookie =
        ResponseCookie.from(REFRESH_TOKEN_NAME, refreshToken)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(Duration.ofMinutes(refreshTokenMaxAge))
            .sameSite("Strict")
            .build();

    response.addHeader("Set-Cookie", cookie.toString());
  }

  public void clearRefreshTokenCookie(HttpServletResponse response) {
    ResponseCookie cookie =
        ResponseCookie.from(REFRESH_TOKEN_NAME, "")
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(Duration.ZERO)
            .sameSite("Strict")
            .build();

    response.addHeader("Set-Cookie", cookie.toString());
  }

  public String getRefreshTokenFromCookie(HttpServletRequest request) {
    if (request.getCookies() == null) {
      return null;
    }

    for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
      if (REFRESH_TOKEN_NAME.equals(cookie.getName())) {
        return cookie.getValue();
      }
    }

    return null;
  }
}
