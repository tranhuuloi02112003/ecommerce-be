package com.lh.ecommerce.security.jwt;

import com.lh.ecommerce.exception.HttpException;
import com.lh.ecommerce.security.SecurityError;
import com.lh.ecommerce.utils.DateUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenHelper {
  private static final String CLAIM_TOKEN_TYPE = "tokenType";
  private static final String TOKEN_TYPE_ACCESS = "access";
  private static final String TOKEN_TYPE_REFRESH = "refresh";

  @Value("${spring.security.secret-key}")
  private String secretKey;

  @Value("${spring.security.access-minutes}")
  private int accessMinutes;

  @Value("${spring.security.refresh-minutes}")
  private int refreshMinutes;

  private SecretKey getSignInKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(String email) {
    return Jwts.builder()
        .subject(email)
        .id(UUID.randomUUID().toString())
        .claim(CLAIM_TOKEN_TYPE, TOKEN_TYPE_ACCESS)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(DateUtils.getTime(accessMinutes))
        .signWith(getSignInKey())
        .compact();
  }

  public String generateRefreshToken(String email, UUID jti) {
    return Jwts.builder()
        .subject(email)
        .id(jti.toString())
        .claim(CLAIM_TOKEN_TYPE, TOKEN_TYPE_REFRESH)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(DateUtils.getTime(refreshMinutes))
        .signWith(getSignInKey())
        .compact();
  }

  public void isAccessToken(String token) throws HttpException {
    String type = extractTokenType(token);
    if (!TOKEN_TYPE_ACCESS.equalsIgnoreCase(type)) {
      throw SecurityError.invalidTokenType().get();
    }
  }

  public void isRefreshToken(String token) throws HttpException {
    String type = extractTokenType(token);
    if (!TOKEN_TYPE_REFRESH.equalsIgnoreCase(type)) {
      throw SecurityError.invalidTokenType().get();
    }
  }

  public String extractJti(String token) {
    return extractClaim(token, Claims::getId);
  }

  public String extractEmail(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = safeExtractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims safeExtractAllClaims(String token) throws HttpException {
    if (token == null || token.isBlank()) {
      throw SecurityError.tokenNotFound().get();
    }
    try {
      return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
    } catch (ExpiredJwtException e) {
      throw SecurityError.expiredToken().get();
    } catch (JwtException | IllegalArgumentException e) {
      throw SecurityError.invalidToken().get();
    }
  }

  private String extractTokenType(String token) {
    return safeExtractAllClaims(token).get(CLAIM_TOKEN_TYPE, String.class);
  }
}
