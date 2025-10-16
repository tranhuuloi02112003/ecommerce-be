package com.lh.ecommerce.dto.response;

import com.lh.ecommerce.enums.OrderStatus;
import com.lh.ecommerce.enums.PaymentMethod;
import com.lh.ecommerce.enums.PaymentStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
  private UUID id;
  private String customerName;
  private String customerPhone;
  private Instant createdAt;
  private int totalItems;
  private double totalAmount;
  private PaymentMethod paymentMethod;
  private PaymentStatus paymentStatus;
  private OrderStatus orderStatus;
}
