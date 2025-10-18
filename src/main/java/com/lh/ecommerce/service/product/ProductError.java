package com.lh.ecommerce.service.product;

import com.lh.ecommerce.exception.BadRequestException;
import com.lh.ecommerce.exception.Error;
import com.lh.ecommerce.exception.NotFoundException;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductError implements Error {
  PRODUCT_NOTFOUND("Product not found"),
  WISH_NOTFOUND("The product is not in your wishlist"),
  WISH_ALREADY_EXISTS("Wish already exists"),
  PRODUCT_NOT_FOUND_IN_LIST("One or more products not found");
  ;
  private final String message;

  @Override
  public String getName() {
    return name();
  }

  public static Supplier<NotFoundException> productNotFound() {
    return () -> new NotFoundException(PRODUCT_NOTFOUND);
  }

  public static Supplier<NotFoundException> wishNotFound() {
    return () -> new NotFoundException(WISH_NOTFOUND);
  }

  public static Supplier<BadRequestException> alreadyExistsWishListItem() {
    return () -> new BadRequestException(WISH_ALREADY_EXISTS);
  }

  public static Supplier<BadRequestException> productNotFoundInList() {
    return () -> new BadRequestException(PRODUCT_NOT_FOUND_IN_LIST);
  }
}
