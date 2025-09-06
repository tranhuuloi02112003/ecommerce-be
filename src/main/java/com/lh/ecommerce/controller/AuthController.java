package com.lh.ecommerce.controller;

import com.lh.ecommerce.dto.response.TokenResponse;
import com.lh.ecommerce.dto.resquest.LoginRequest;
import com.lh.ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/login")
  public TokenResponse login(@RequestBody LoginRequest loginRequest) {
    return authService.login(loginRequest);
  }
}
