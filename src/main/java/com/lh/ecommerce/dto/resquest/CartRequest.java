package com.lh.ecommerce.dto.resquest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CartRequest {
  @NotNull(message = "ProductId must not be null")
  private UUID productId;

  @Min(value = 1, message = "Quantity must be greater than 0")
  private int quantity;
}
