package com.lh.ecommerce.repository.redis;

import com.lh.ecommerce.dto.response.Session;
import java.time.Duration;

public interface RedisRepository {
  void blacklistAccessJti(String tokenId, Duration ttl);

  boolean isAccessJtiBlacklisted(String tokenId);

  void saveSession(Session session);

  boolean existsByRefreshJti(String refreshJti);

  void deleteByRefreshJti(String refreshJti);
}
