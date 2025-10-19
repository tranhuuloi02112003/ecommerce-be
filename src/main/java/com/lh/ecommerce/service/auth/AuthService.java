package com.lh.ecommerce.service.auth;

import com.lh.ecommerce.adapter.CookieAdapter;
import com.lh.ecommerce.adapter.NotificationAdapter;
import com.lh.ecommerce.dto.response.AccessTokenResponse;
import com.lh.ecommerce.dto.response.Session;
import com.lh.ecommerce.dto.response.UserResponse;
import com.lh.ecommerce.dto.resquest.LoginRequest;
import com.lh.ecommerce.dto.resquest.RegisterRequest;
import com.lh.ecommerce.entity.UserEntity;
import com.lh.ecommerce.mapper.UserMapper;
import com.lh.ecommerce.repository.RedisRepository;
import com.lh.ecommerce.repository.UserRepository;
import com.lh.ecommerce.security.SecurityError;
import com.lh.ecommerce.security.jwt.JwtTokenHelper;
import com.lh.ecommerce.service.user.UserError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
  private static final String BEARER_PREFIX = "Bearer ";

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenHelper jwtTokenHelper;
  private final RedisRepository redisRepository;
  private final UserRepository userRepository;
  private final CookieAdapter cookieService;
  private final UserMapper userMapper;
  private final NotificationAdapter notificationAdapter;

  @Transactional
  public AccessTokenResponse login(LoginRequest request, HttpServletResponse response) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    String accessToken = jwtTokenHelper.generateToken(request.getEmail());
    UUID jtiRefreshToken = UUID.randomUUID();
    String refreshToken = jwtTokenHelper.generateRefreshToken(request.getEmail(), jtiRefreshToken);

    UserEntity user =
        userRepository.findByEmail(request.getEmail()).orElseThrow(UserError.userNotFound());

    Session session =
        Session.builder()
            .refreshJti(jtiRefreshToken)
            .userId(user.getId())
            .email(user.getEmail())
            .build();

    redisRepository.saveSession(session);
    cookieService.setRefreshTokenCookie(response, refreshToken);

    return AccessTokenResponse.builder().accessToken(accessToken).build();
  }

  @Transactional
  public void logout(String bearerToken, HttpServletResponse response) {
    if (bearerToken == null || !bearerToken.startsWith(BEARER_PREFIX)) {
      throw SecurityError.invalidToken().get();
    }

    String access = bearerToken.substring(BEARER_PREFIX.length());

    Instant exp = jwtTokenHelper.extractExpiration(access).toInstant();
    long ttlSec = Math.max(0L, Duration.between(Instant.now(), exp).getSeconds());

    if (ttlSec > 0) {
      String jti = jwtTokenHelper.extractJti(access);
      redisRepository.blacklistAccessJti(jti, Duration.ofSeconds(ttlSec));
    }
    cookieService.clearRefreshTokenCookie(response);
  }

  @Transactional
  public AccessTokenResponse refresh(HttpServletRequest request, HttpServletResponse response) {
    String refresh = cookieService.getRefreshTokenFromCookie(request);
    jwtTokenHelper.isRefreshToken(refresh);

    String email = jwtTokenHelper.extractEmail(refresh);
    String jtiRefreshToken = jwtTokenHelper.extractJti(refresh);
    validateRefreshToken(email, jtiRefreshToken);

    String accessToken = jwtTokenHelper.generateToken(email);

    cookieService.setRefreshTokenCookie(response, refresh);
    return AccessTokenResponse.builder().accessToken(accessToken).build();
  }

  @Transactional
  public UserResponse register(RegisterRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw UserError.emailAlreadyExists().get();
    }

    UserEntity user = userMapper.toUserEntity(request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    String verificationCode = UUID.randomUUID().toString();
    user.setVerificationCode(verificationCode);

    UserEntity saved = userRepository.save(user);

    String fullName = request.getFirstName() + " " + request.getLastName();
    notificationAdapter.sendVerificationEmail(saved.getEmail(), verificationCode, fullName);

    return userMapper.toResponse(saved);
  }

  @Transactional
  public void verifyEmail(String token) {
    UserEntity user =
        userRepository
            .findByVerificationCode(token)
            .orElseThrow(UserError.invalidVerificationToken());

    if (user.isEnabled()) {
      throw UserError.accountAlreadyVerified().get();
    }

    user.setEnabled(true);
    user.setVerificationCode(null);
    userRepository.save(user);
  }

  private void validateRefreshToken(String email, String jtiRefreshToken) {
    if (jtiRefreshToken == null || jtiRefreshToken.isBlank()) {
      throw SecurityError.refreshTokenJtiInvalid().get();
    }
    if (!userRepository.existsByEmail(email)) {
      throw SecurityError.emailNotFound().get();
    }
    if (!redisRepository.existsByRefreshJti(jtiRefreshToken)) {
      throw SecurityError.refreshTokenRotated().get();
    }
  }
}
