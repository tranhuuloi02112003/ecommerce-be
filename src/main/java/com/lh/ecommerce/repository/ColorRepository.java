package com.lh.ecommerce.repository;

import com.lh.ecommerce.entity.ColorEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<ColorEntity, UUID> {}
