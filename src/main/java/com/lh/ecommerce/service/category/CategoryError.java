package com.lh.ecommerce.service.category;

import com.lh.ecommerce.exception.Error;
import com.lh.ecommerce.exception.NotFoundException;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryError implements Error {
  CATEGORY_NOTFOUND("Category not found"),
  ;
  private final String message;

  @Override
  public String getName() {
    return name();
  }

  public static Supplier<NotFoundException> categoryNotFound() {
    return () -> new NotFoundException(CATEGORY_NOTFOUND);
  }
}
