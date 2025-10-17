package com.lh.ecommerce.repository;

import com.lh.ecommerce.entity.AddressEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, UUID> {

  List<AddressEntity> findByUserId(UUID userId);

  Optional<AddressEntity> findByIdAndUserId(UUID id, UUID userId);

  boolean existsByIdAndUserId(UUID id, UUID userId);

  void deleteByIdAndUserId(UUID id, UUID userId);

  @Modifying(flushAutomatically = true, clearAutomatically = true)
  @Query(
      """
           update AddressEntity a
              set a.defaultAddress = false
            where a.userId = :userId and a.defaultAddress = true
           """)
  void clearDefaultByUserId(UUID userId);
}
