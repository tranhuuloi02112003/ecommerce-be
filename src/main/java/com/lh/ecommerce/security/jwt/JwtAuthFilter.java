package com.lh.ecommerce.security.jwt;

import com.lh.ecommerce.entity.UserEntity;
import com.lh.ecommerce.repository.RedisRepository;
import com.lh.ecommerce.repository.UserRepository;
import com.lh.ecommerce.security.SecurityError;
import com.lh.ecommerce.security.user.CustomUserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
  private static final String BEARER_PREFIX = "Bearer ";

  private final UserRepository userRepository;
  private final JwtTokenHelper jwtTokenHelper;
  private final RedisRepository redisRepository;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (bearerToken == null || !bearerToken.startsWith(BEARER_PREFIX)) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = bearerToken.replace(BEARER_PREFIX, "");
    jwtTokenHelper.isAccessToken(token);

    String jti = jwtTokenHelper.extractJti(token);
    if (redisRepository.isAccessJtiBlacklisted(jti)) {
      throw SecurityError.logoutToken().get();
    }

    String email = jwtTokenHelper.extractEmail(token);

    if (email != null) {
      UserEntity userEntity = userRepository.findByEmail(email).orElseThrow();

      CustomUserPrincipal customUserPrincipal =
          CustomUserPrincipal.builder()
              .email(email)
              .password(userEntity.getPassword())
              .id(userEntity.getId())
              .build();

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(
              customUserPrincipal, null, customUserPrincipal.getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(request, response);
  }
}
