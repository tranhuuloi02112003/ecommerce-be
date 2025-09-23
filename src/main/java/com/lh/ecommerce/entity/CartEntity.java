package com.lh.ecommerce.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private int quantity;

  @Column(name = "user_id")
  private UUID userId;

  @Column(name = "product_id")
  private UUID productId;

  @Transient private ProductEntity productEntity;
  @Transient private ImageEntity imageEntity;

  CartEntity(CartEntity cartEntity, ProductEntity productEntity, ImageEntity imageEntity) {
    this.id = cartEntity.getId();
    this.quantity = cartEntity.getQuantity();
    this.userId = cartEntity.getUserId();
    this.productId = productEntity.getId();
    this.productEntity = productEntity;
    this.imageEntity = imageEntity;
  }
}
