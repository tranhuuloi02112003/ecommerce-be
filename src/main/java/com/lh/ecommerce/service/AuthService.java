package com.lh.ecommerce.service;

import com.lh.ecommerce.dto.response.TokenResponse;
import com.lh.ecommerce.dto.resquest.LoginRequest;

public interface AuthService {
  TokenResponse login(LoginRequest request);

  void logout(String bearerToken);
}
