package com.lh.ecommerce.service.order;

import com.lh.ecommerce.exception.BadRequestException;
import com.lh.ecommerce.exception.Error;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderError implements Error {
  INSUFFICIENT_STOCK("Insufficient stock for one or more products"),
  DUPLICATE_PRODUCT("Duplicate product items in order");

  private final String message;

  @Override
  public String getName() {
    return name();
  }

  public static Supplier<BadRequestException> duplicateProductInOrder() {
    return () -> new BadRequestException(DUPLICATE_PRODUCT);
  }

  public static Supplier<BadRequestException> insufficientStock() {
    return () -> new BadRequestException(INSUFFICIENT_STOCK);
  }
}
