package com.lh.ecommerce.repository;

import com.lh.ecommerce.entity.ProductColorEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductColorRepository extends JpaRepository<ProductColorEntity, UUID> {}
