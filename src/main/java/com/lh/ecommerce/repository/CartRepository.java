package com.lh.ecommerce.repository;

import com.lh.ecommerce.entity.CartEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, UUID> {
  @Query(
      """
    SELECT NEW CartEntity(c, p, i)
    FROM CartEntity c
    JOIN ProductEntity p ON c.productId = p.id
    JOIN ImageEntity i ON p.id = i.productId
    WHERE c.userId = :userId AND i.isMain =true
    """)
  List<CartEntity> findByUserId(UUID userId);

  @Modifying
  @Query("DELETE FROM CartEntity c WHERE c.userId = :userId AND c.productId = :productId")
  int deleteOne(UUID userId, UUID productId);

  @Modifying
  @Query(
      value =
          """
        UPDATE carts c
        SET quantity = :quantity
        FROM products p
        WHERE c.user_id = :userId
          AND c.product_id = :productId
          AND p.id = c.product_id
          AND p.quantity >= :quantity
        """,
      nativeQuery = true)
  int updateQuantityWithStock(UUID userId, UUID productId, int quantity);

  @Query(
      "SELECT COUNT(c)>0 FROM CartEntity c WHERE c.userId = :userId AND c.productId = :productId")
  boolean existsByUserIdAndProductId(UUID userId, UUID productId);
}
