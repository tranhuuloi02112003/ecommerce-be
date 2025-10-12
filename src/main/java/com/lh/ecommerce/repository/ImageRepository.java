package com.lh.ecommerce.repository;

import com.lh.ecommerce.entity.ImageEntity;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, UUID> {
  void deleteByProductId(UUID productId);

  @Query(
      """
    SELECT i
    FROM ImageEntity i
    WHERE i.productId IN :ids AND i.isMain = true
""")
  List<ImageEntity> findMainByProductIds(@Param("ids") Collection<UUID> ids);

  @Query(
      """
  SELECT i.key
  FROM ImageEntity i
  WHERE i.productId = :productId
  ORDER BY i.isMain DESC
""")
  List<String> findKeysByProductId(@Param("productId") UUID productId);
}
