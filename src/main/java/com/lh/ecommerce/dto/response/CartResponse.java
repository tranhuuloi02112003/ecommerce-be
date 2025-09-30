package com.lh.ecommerce.dto.response;

import java.util.UUID;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {
  private UUID productId;
  private String productName;
  private String productMainImage;
  private double price;
  private int quantity;
}
