package com.lh.ecommerce.utils;

import com.lh.ecommerce.dto.response.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PageUtils {
  public Pageable pageableFromClient(int page1Based, int size) {
    int p = Math.max(0, page1Based - 1);
    int s = (size <= 0 || size > 100) ? 20 : size;
    return PageRequest.of(p, s);
  }

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
