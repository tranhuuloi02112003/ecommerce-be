package com.lh.ecommerce.repository;

import com.lh.ecommerce.entity.OrderEntity;
import com.lh.ecommerce.enums.OrderStatus;
import com.lh.ecommerce.enums.PaymentStatus;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
  @Query(
      """
  select new OrderEntity(
    o,
    a,
    (select COUNT(oi.id)
       FROM OrderItemEntity oi
      WHERE oi.orderId = o.id)
  )
  from OrderEntity o
  left join AddressEntity a ON a.id = o.addressId
  where (cast(o.id AS string) ilike concat('%', :search, '%')
        or cast(o.paymentMethod AS string) ilike concat('%', :search, '%')
        or a.name ilike concat('%', :search, '%')
        or a.phone ilike concat('%', :search, '%'))
        and (:orderStatus is null or o.orderStatus = :orderStatus)
        and (:paymentStatus is null or o.paymentStatus = :paymentStatus)
        and (cast(:startDate as timestamp) is null or o.createdAt >= :startDate)
        and (cast(:endDate as timestamp)   is null or o.createdAt <  :endDate)
  order by o.createdAt DESC
  """)
  Page<OrderEntity> searchWithFilters(
      String search,
      OrderStatus orderStatus,
      PaymentStatus paymentStatus,
      Instant startDate,
      Instant endDate,
      Pageable pageable);
}
