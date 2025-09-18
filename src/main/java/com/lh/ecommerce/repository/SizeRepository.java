package com.lh.ecommerce.repository;

import com.lh.ecommerce.entity.SizeEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends JpaRepository<SizeEntity, UUID> {
}
