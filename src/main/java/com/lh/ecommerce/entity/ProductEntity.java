package com.lh.ecommerce.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity extends BaseAuditEntity {
  private String name;
  private String description;
  private double price;
  private int quantity;

  @Column(name = "category_id")
  private UUID categoryId;

  @Transient private ImageEntity mainImage;

  @Transient private CategoryEntity category;

  @Transient private boolean isWish;

  @Transient private boolean isNew;

  ProductEntity(ProductEntity product, ImageEntity image, CategoryEntity category) {
    super(
        product.getId(),
        product.getCreatedAt(),
        product.getUpdatedAt(),
        product.getCreatedBy(),
        product.getUpdatedBy());
    this.name = product.getName();
    this.description = product.getDescription();
    this.price = product.getPrice();
    this.categoryId = product.getCategoryId();
    this.mainImage = image;
    this.category = category;
    this.quantity = product.getQuantity();
  }

  ProductEntity(ProductEntity product, ImageEntity image) {
    super(
        product.getId(),
        product.getCreatedAt(),
        product.getUpdatedAt(),
        product.getCreatedBy(),
        product.getUpdatedBy());
    this.name = product.getName();
    this.description = product.getDescription();
    this.price = product.getPrice();
    this.categoryId = product.getCategoryId();
    this.mainImage = image;
    this.quantity = product.getQuantity();
  }

  public ProductEntity(ProductEntity product, ImageEntity image, boolean isWish, boolean isNew) {
    super(
        product.getId(),
        product.getCreatedAt(),
        product.getUpdatedAt(),
        product.getCreatedBy(),
        product.getUpdatedBy());
    this.name = product.getName();
    this.description = product.getDescription();
    this.price = product.getPrice();
    this.quantity = product.getQuantity();
    this.categoryId = product.getCategoryId();
    this.mainImage = image;
    this.isWish = isWish;
    this.isNew = isNew;
  }
}
