package com.lh.ecommerce.service.auth;

import com.lh.ecommerce.dto.response.RefreshRequest;
import com.lh.ecommerce.dto.response.Session;
import com.lh.ecommerce.dto.response.TokenResponse;
import com.lh.ecommerce.dto.resquest.LoginRequest;
import com.lh.ecommerce.entity.UserEntity;
import com.lh.ecommerce.repository.redis.RedisRepository;
import com.lh.ecommerce.security.jwt.JwtTokenHelper;
import com.lh.ecommerce.service.user.UserService;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private static final String BEARER_PREFIX = "Bearer ";
  private static final String TOKEN_TYPE_REFRESH = "refresh";

  private final AuthenticationManager authenticationManager;
  private final JwtTokenHelper jwtTokenHelper;
  private final RedisRepository redisRepository;
  private final UserService userService;

  @Override
  public TokenResponse login(LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    String accessToken = jwtTokenHelper.generateToken(request.getEmail());
    UUID jtiRefreshToken = UUID.randomUUID();
    String refreshToken = jwtTokenHelper.generateRefreshToken(request.getEmail(), jtiRefreshToken);

    UserEntity user = userService.getUserByEmail(request.getEmail());

    Session session =
        Session.builder()
            .refreshJti(jtiRefreshToken)
            .userId(user.getId())
            .email(user.getEmail())
            .build();

    redisRepository.saveSession(session);
    return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
  }

  @Override
  @Transactional
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

  @Override
  @Transactional
  public TokenResponse refresh(RefreshRequest request) {
    String refresh = request.getRefreshToken();
    if (refresh == null || refresh.isBlank()) {
      throw new BadCredentialsException("Refresh token is missing");
    }

    String tokenType = jwtTokenHelper.extractTokenType(refresh);
    if (!TOKEN_TYPE_REFRESH.equals(tokenType)) {
      throw new BadCredentialsException("Invalid token type");
    }

    String email = jwtTokenHelper.extractEmail(refresh);
    String jtiRefreshToken = jwtTokenHelper.extractJti(refresh);
    UserEntity user = userService.getUserByEmail(email);
    if (jtiRefreshToken == null || jtiRefreshToken.isBlank()) {
      throw new BadCredentialsException("Invalid refresh token id");
    }

    if (!redisRepository.existsByRefreshJti(jtiRefreshToken)) {
      throw new BadCredentialsException("Refresh token is invalid or rotated");
    }
    redisRepository.deleteByRefreshJti(jtiRefreshToken);

    String accessToken = jwtTokenHelper.generateToken(email);
    UUID jtiRefreshTokenNew = UUID.randomUUID();
    String refreshToken = jwtTokenHelper.generateRefreshToken(email, jtiRefreshTokenNew);
    Session session =
        Session.builder()
            .refreshJti(jtiRefreshTokenNew)
            .userId(user.getId())
            .email(user.getEmail())
            .build();
    redisRepository.saveSession(session);
    return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
  }
}
