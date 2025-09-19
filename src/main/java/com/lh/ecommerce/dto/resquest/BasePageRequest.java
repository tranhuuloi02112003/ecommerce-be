package com.lh.ecommerce.dto.resquest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
public class BasePageRequest {
  @Min(0)
  private int page = 0;

  @Min(1)
  @Max(200)
  private int size;

  public Pageable getPageable() {
    return PageRequest.of(getPage(), getSize());
  }

  private int getSize() {
    return size < 10 ? 10 : size;
  }

  private int getPage() {
    return page >= 1 ? page - 1 : page;
  }
}
