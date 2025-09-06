package com.lh.ecommerce.repository.redis;

import java.time.Duration;

public interface RedisRepository {
  void blacklistAccessJti(String tokenId, Duration ttl);

  boolean isAccessJtiBlacklisted(String tokenId);
}
