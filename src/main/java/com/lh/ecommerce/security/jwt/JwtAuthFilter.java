package com.lh.ecommerce.security.jwt;

import com.lh.ecommerce.entity.UserEntity;
import com.lh.ecommerce.repository.redis.RedisRepository;
import com.lh.ecommerce.security.user.CustomUserPrincipal;
import com.lh.ecommerce.service.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
  private static final String BEARER_PREFIX = "Bearer ";

  private final UserService userService;
  private final JwtTokenHelper jwtTokenHelper;
  private final RedisRepository redisRepository;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (bearerToken == null || !bearerToken.startsWith(BEARER_PREFIX)) {
      filterChain.doFilter(request, response);
    }

    String token = bearerToken.replace(BEARER_PREFIX, "");
    String jti = jwtTokenHelper.extractJti(token);
    if (redisRepository.isAccessJtiBlacklisted(jti)) {
      throw new BadCredentialsException("Token is logged out");
    }
    String username = jwtTokenHelper.extractUsername(token);
    if (username != null) {
      UserEntity userEntity = userService.getUserByUsername(username);

      CustomUserPrincipal customUserPrincipal =
          CustomUserPrincipal.builder()
              .username(username)
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
