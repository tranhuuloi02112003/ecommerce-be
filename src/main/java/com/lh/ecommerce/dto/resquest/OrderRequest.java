package com.lh.ecommerce.dto.resquest;

import com.lh.ecommerce.enums.PaymentMethod;
import com.lh.ecommerce.service.order.OrderError;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
public class OrderRequest {

  @NotNull(message = "Address ID must not be null")
  private UUID addressId;

  @NotNull(message = "Payment method not be null")
  private PaymentMethod paymentMethod;

  private String note;

  @NotNull(message = "Total amount must not be null")
  @DecimalMin(value = "0.0", inclusive = false, message = "Total amount must be greater than 0")
  private double totalAmount;

  @NotEmpty(message = "Product list must not be empty")
  private List<OrderItemRequest> productItems;

  public List<OrderItemRequest> getProductItems() {
    long distinctCount =
        productItems.stream().map(OrderItemRequest::getProductId).distinct().count();
    if (distinctCount != productItems.size()) {
      throw OrderError.duplicateProductInOrder().get();
    }
    return productItems.stream().distinct().toList();
  }
}
