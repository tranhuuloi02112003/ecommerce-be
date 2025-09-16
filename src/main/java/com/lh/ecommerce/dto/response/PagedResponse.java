package com.lh.ecommerce.dto.response;

import java.util.List;

public record PagedResponse<T>(List<T> data, PaginationMeta pagination) {
  public static record PaginationMeta(
      int currentPage,
      int totalPages,
      long totalElements,
      int size,
      boolean hasNext,
      boolean hasPrev) {}
}
