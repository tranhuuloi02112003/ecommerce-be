package com.lh.ecommerce.dto.response;

import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductBasicResponse {
  private UUID id;
  private String name;
  private String description;
  private Double price;
  private String categoryName;
  private String mainImage;
}
