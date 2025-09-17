package com.lh.ecommerce.dto.resquest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BasePageRequest {
  @Min(0)
  private int page = 0;

  @Min(1)
  @Max(200)
  private int size = 20;
}
