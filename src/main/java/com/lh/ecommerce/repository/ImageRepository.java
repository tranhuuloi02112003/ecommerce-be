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
      value =
          """
    SELECT DISTINCT ON (product_id) *
    FROM images
    WHERE product_id IN (:ids)
    ORDER BY product_id, id ASC
""",
      nativeQuery = true)
  List<ImageEntity> findFirstByProductIds(@Param("ids") Collection<UUID> ids);
}
