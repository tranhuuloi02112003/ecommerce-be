package com.lh.ecommerce.repository;

import com.lh.ecommerce.entity.ProductColorEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductColorRepository extends JpaRepository<ProductColorEntity, UUID> {
  @Query("SELECT pc.colorId FROM ProductColorEntity pc WHERE pc.productId = :productId")
  List<UUID> findColorIdsByProductId(@Param("productId") UUID productId);

  void deleteByProductId(UUID productId);
}
