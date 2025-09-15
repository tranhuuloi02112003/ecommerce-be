package com.lh.ecommerce.repository;

import com.lh.ecommerce.entity.ImageEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, UUID> {
  void deleteByProductId(UUID productId);
}
