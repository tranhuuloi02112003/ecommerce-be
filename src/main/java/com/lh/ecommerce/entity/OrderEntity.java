package com.lh.ecommerce.entity;

import com.lh.ecommerce.enums.OrderStatus;
import com.lh.ecommerce.enums.PaymentMethod;
import com.lh.ecommerce.enums.PaymentStatus;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity extends BaseAuditEntity {

  @Column(name = "user_id")
  private UUID userId;

  @Column(name = "address_id")
  private UUID addressId;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_method")
  private PaymentMethod paymentMethod;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_status")
  private PaymentStatus paymentStatus;

  @Enumerated(EnumType.STRING)
  @Column(name = "order_status")
  private OrderStatus orderStatus;

  private Double totalAmount;

  @Transient private AddressEntity address;

  @Transient private long totalItems;

  public OrderEntity(OrderEntity order, AddressEntity address, long totalItems) {
    super(
        order.getId(),
        order.getCreatedAt(),
        order.getUpdatedAt(),
        order.getCreatedBy(),
        order.getUpdatedBy());
    this.userId = order.getUserId();
    this.addressId = order.getAddressId();
    this.paymentMethod = order.getPaymentMethod();
    this.paymentStatus = order.getPaymentStatus();
    this.orderStatus = order.getOrderStatus();
    this.totalAmount = order.getTotalAmount();
    this.totalItems = totalItems;
    this.address = address;
  }
}
