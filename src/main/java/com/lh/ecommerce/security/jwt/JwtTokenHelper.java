package com.lh.ecommerce.security.jwt;

import com.lh.ecommerce.utils.DateUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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

  @Value("${spring.security.secret-key}")
  private String secretKey;

  @Value("${spring.security.access-minutes}")
  private int accessMinutes;

  private SecretKey getSignInKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(String username) {
    return Jwts.builder()
        .subject(username)
        .id(UUID.randomUUID().toString())
        .claim(CLAIM_TOKEN_TYPE, TOKEN_TYPE_ACCESS)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(DateUtils.getTime(accessMinutes))
        .signWith(getSignInKey())
        .compact();
  }

  public String extractTokenType(String token) {
    return extractAllClaims(token).get(CLAIM_TOKEN_TYPE, String.class);
  }

  public String extractJti(String token) {
    return extractClaim(token, Claims::getId);
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
  }
}
