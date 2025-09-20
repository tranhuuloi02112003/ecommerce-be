package com.lh.ecommerce.dto.resquest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCriteriaRequest extends BasePageRequest {
  private String search;

  public String getSearch() {
    return search != null ? search.trim() : "";
  }
}
