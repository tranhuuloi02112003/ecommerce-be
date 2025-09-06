package com.lh.ecommerce.repository.redis;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {
  private final RedisTemplate<String, Object> redis;

  private static String blAtkKey(String jti) {
    return "bl:atk:%s".formatted(jti);
  }

  @Override
  public void blacklistAccessJti(String tokenId, Duration ttl) {
    redis.opsForValue().set(blAtkKey(tokenId), "1", ttl);
  }

  @Override
  public boolean isAccessJtiBlacklisted(String tokenId) {
    return redis.hasKey(blAtkKey(tokenId));
  }
}
