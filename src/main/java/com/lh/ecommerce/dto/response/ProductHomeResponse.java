package com.lh.ecommerce.dto.response;

import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductHomeResponse {
  private UUID id;
  private String name;
  private double price;
  private String mainImage;
  private boolean isNew;
}
