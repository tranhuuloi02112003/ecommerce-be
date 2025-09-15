package com.lh.ecommerce.service.product;

import com.lh.ecommerce.exception.Error;
import com.lh.ecommerce.exception.NotFoundException;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductError implements Error {
  PRODUCT_NOTFOUND("Product not found"),;
  private final String message;

  @Override
  public String getName() {
    return name();
  }

  public static Supplier<NotFoundException> productNotFound() {
    return () -> new NotFoundException(PRODUCT_NOTFOUND);
  }
}
