package com.lh.ecommerce.repository;

import com.lh.ecommerce.entity.WishEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<WishEntity, UUID> {
  boolean existsByUserIdAndProductId(UUID userId, UUID productId);

  int deleteByUserIdAndProductId(UUID userId, UUID productId);

  @Query("SELECT w.productId FROM WishEntity w WHERE w.userId = :userId")
  List<UUID> findProductIdsByUserId(UUID userId);
}
