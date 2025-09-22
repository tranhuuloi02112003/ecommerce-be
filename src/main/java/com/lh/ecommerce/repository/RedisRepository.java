package com.lh.ecommerce.repository;

import com.lh.ecommerce.dto.response.Session;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepository {
  private final RedisTemplate<String, Object> redis;

  @Value("${spring.security.refresh-minutes}")
  private int refreshMinutes;

  private static String blAtkKey(String jti) {
    return "bl:atk:%s".formatted(jti);
  }

  private String sessionKey(String refreshJti) {
    return "sess:" + refreshJti;
  }

  public void blacklistAccessJti(String tokenId, Duration ttl) {
    redis.opsForValue().set(blAtkKey(tokenId), "1", ttl);
  }

  public boolean isAccessJtiBlacklisted(String tokenId) {
    return redis.hasKey(blAtkKey(tokenId));
  }

  public void saveSession(Session session) {
    redis
        .opsForValue()
        .set(
            sessionKey(session.getRefreshJti().toString()),
            session,
            Duration.ofMinutes(refreshMinutes));
  }

  public boolean existsByRefreshJti(String refreshJti) {
    return redis.hasKey(sessionKey(refreshJti));
  }
}
