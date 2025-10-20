package com.lh.ecommerce.dto.resquest;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemRequest {
  @NotNull(message = "Product ID must not be null")
  private UUID productId;

  @NotNull(message = "Quantity must not be null")
  @Min(value = 1, message = "Quantity must be at least 1")
  private int quantity;

  @NotNull(message = "Price amount must not be null")
  @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
  private double price;
}
