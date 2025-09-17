package com.lh.ecommerce.utils;

import com.lh.ecommerce.dto.response.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageUtils {

  public PagedResponse.PaginationMeta toMeta(Page<?> page) {
    return new PagedResponse.PaginationMeta(
        page.getNumber() + 1,
        page.getTotalPages(),
        page.getTotalElements(),
        page.getSize(),
        page.hasNext(),
        page.hasPrevious());
  }
}
