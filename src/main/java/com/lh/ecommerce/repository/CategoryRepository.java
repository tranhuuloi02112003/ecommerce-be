package com.lh.ecommerce.repository;

import com.lh.ecommerce.entity.CategoryEntity;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
  List<CategoryEntity> findByIdIn(Collection<UUID> ids);
}
