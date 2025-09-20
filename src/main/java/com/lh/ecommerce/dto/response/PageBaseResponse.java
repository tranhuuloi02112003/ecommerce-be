package com.lh.ecommerce.dto.response;

import java.util.List;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageBaseResponse<T> {
  private List<T> data;
  private Pagination pagination;

  public PageBaseResponse(List<T> data, Page<?> page) {
    this.data = data;
    this.pagination =
        Pagination.builder()
            .currentPage(page.getNumber() + 1)
            .totalPages(page.getTotalPages())
            .size(page.getSize())
            .hasNext(page.hasNext())
            .hasPrev(page.hasPrevious())
            .build();
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Pagination {
    private int currentPage;
    private int totalPages;
    private int size;
    private long totalElements;
    private boolean hasNext;
    private boolean hasPrev;
  }
}
