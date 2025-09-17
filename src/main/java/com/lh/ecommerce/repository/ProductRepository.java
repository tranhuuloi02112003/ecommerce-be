package com.lh.ecommerce.repository;

import com.lh.ecommerce.entity.ProductEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
  @Query(
      """
    SELECT p FROM ProductEntity p
    JOIN CategoryEntity c ON p.categoryId = c.id
    WHERE (:search IS NULL
       OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')))
    """)
  Page<ProductEntity> search(@Param("search") String search, Pageable pageable);
}
