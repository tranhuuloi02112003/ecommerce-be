package com.lh.ecommerce.service.auth;

import com.lh.ecommerce.dto.response.TokenResponse;
import com.lh.ecommerce.dto.resquest.LoginRequest;
import com.lh.ecommerce.repository.redis.RedisRepository;
import com.lh.ecommerce.security.jwt.JwtTokenHelper;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final AuthenticationManager authenticationManager;
  private final JwtTokenHelper jwtTokenHelper;
  private final RedisRepository redisRepository;
  private static final String BEARER_PREFIX = "Bearer ";

  @Override
  public TokenResponse login(LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    String accessToken = jwtTokenHelper.generateToken(request.getUsername());
    return TokenResponse.builder().accessToken(accessToken).build();
  }

  @Override
  public void logout(String bearerToken) {
    if (bearerToken == null || !bearerToken.startsWith(BEARER_PREFIX)) {
      throw new BadCredentialsException("Authorization header is missing or invalid");
    }

    String access = bearerToken.substring(BEARER_PREFIX.length());

    Instant exp = jwtTokenHelper.extractExpiration(access).toInstant();
    long ttlSec = Math.max(0L, Duration.between(Instant.now(), exp).getSeconds());

    if (ttlSec > 0) {
      String jti = jwtTokenHelper.extractJti(access);
      redisRepository.blacklistAccessJti(jti, Duration.ofSeconds(ttlSec));
    }
  }
}
