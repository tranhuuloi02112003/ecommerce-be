package com.lh.ecommerce.dto.response;

import java.util.UUID;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {
  private UUID id;
  private String name;
}
