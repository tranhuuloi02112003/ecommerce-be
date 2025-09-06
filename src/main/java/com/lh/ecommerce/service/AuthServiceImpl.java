package com.lh.ecommerce.service;

import com.lh.ecommerce.dto.response.TokenResponse;
import com.lh.ecommerce.dto.resquest.LoginRequest;
import com.lh.ecommerce.security.jwt.JwtTokenHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final AuthenticationManager authenticationManager;
  private final JwtTokenHelper jwtTokenHelper;

  @Override
  public TokenResponse login(LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    String accessToken = jwtTokenHelper.generateToken(request.getUsername());
    return TokenResponse.builder().accessToken(accessToken).build();
  }
}
