package com.lh.ecommerce.repository;

import com.lh.ecommerce.entity.ProductSizeEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductSizeRepository extends JpaRepository<ProductSizeEntity, UUID> {
  @Query("SELECT ps.sizeId FROM ProductSizeEntity ps WHERE ps.productId = :productId")
  List<UUID> findSizeIdByProductId(@Param("productId") UUID productId);
}
