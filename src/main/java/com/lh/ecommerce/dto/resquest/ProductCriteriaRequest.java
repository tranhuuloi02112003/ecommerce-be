package com.lh.ecommerce.dto.resquest;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class ProductCriteriaRequest extends BasePageRequest {
  private String search;

  public String getSearch() {
    return StringUtils.trimToEmpty(search);
  }
}
