package com.lh.ecommerce.service.cart;

import com.lh.ecommerce.exception.BadRequestException;
import com.lh.ecommerce.exception.Error;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CartError implements Error {
  PRODUCT_NOT_IN_CART("Product is not in user's cart"),
  QUANTITY_EXCEEDS_STOCK("Requested quantity exceeds available stock");
  private final String message;

  @Override
  public String getName() {
    return name();
  }

  public static Supplier<BadRequestException> productNotInCart() {
    return () -> new BadRequestException(PRODUCT_NOT_IN_CART);
  }

  public static Supplier<BadRequestException> quantityExceedsStock() {
    return () -> new BadRequestException(QUANTITY_EXCEEDS_STOCK);
  }
}
