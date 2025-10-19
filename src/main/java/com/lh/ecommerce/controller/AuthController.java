package com.lh.ecommerce.controller;

import com.lh.ecommerce.dto.response.AccessTokenResponse;
import com.lh.ecommerce.dto.response.UserResponse;
import com.lh.ecommerce.dto.resquest.LoginRequest;
import com.lh.ecommerce.dto.resquest.RegisterRequest;
import com.lh.ecommerce.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/token")
  public AccessTokenResponse login(
      @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    return authService.login(loginRequest, response);
  }

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void logout(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
      HttpServletResponse response) {
    authService.logout(authorization, response);
  }

  @PostMapping("/refresh-token")
  public AccessTokenResponse refreshToken(
      HttpServletRequest request, HttpServletResponse response) {
    return authService.refresh(request, response);
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponse register(@Valid @RequestBody RegisterRequest request) {
    return authService.register(request);
  }

  @GetMapping("/verify-email")
  public void verifyEmail(@RequestParam("token") String token) {
    authService.verifyEmail(token);
  }
}
