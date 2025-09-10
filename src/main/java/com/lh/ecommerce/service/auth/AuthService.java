package com.lh.ecommerce.service.auth;

import com.lh.ecommerce.dto.response.TokenResponse;
import com.lh.ecommerce.dto.resquest.LoginRequest;

public interface AuthService {
  TokenResponse login(LoginRequest request);
}
