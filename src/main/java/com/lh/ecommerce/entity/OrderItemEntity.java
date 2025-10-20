package com.lh.ecommerce.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "order_id")
  private UUID orderId;

  @Column(name = "product_id")
  private UUID productId;

  private int quantity;

  private Double price;

  @Transient private ProductEntity product;

  public OrderItemEntity(OrderItemEntity item, ProductEntity product) {
    this.id = item.getId();
    this.orderId = item.getOrderId();
    this.productId = item.getProductId();
    this.quantity = item.getQuantity();
    this.price = item.getPrice();
    this.product = product;
  }
}
