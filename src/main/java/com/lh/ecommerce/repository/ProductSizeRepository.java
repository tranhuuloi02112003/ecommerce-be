package com.lh.ecommerce.repository;

import com.lh.ecommerce.entity.ProductSizeEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSizeRepository extends JpaRepository<ProductSizeEntity, UUID> {}
