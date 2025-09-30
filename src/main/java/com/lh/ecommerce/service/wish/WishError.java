package com.lh.ecommerce.service.wish;

import com.lh.ecommerce.exception.BadRequestException;
import com.lh.ecommerce.exception.Error;
import com.lh.ecommerce.exception.NotFoundException;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WishError implements Error {
  WISH_NOTFOUND("The product is not in your wishlist"),
  WISH_ALREADY_EXISTS("Wish already exists");
  ;
  private final String message;

  @Override
  public String getName() {
    return name();
  }

  public static Supplier<NotFoundException> wishNotFound() {
    return () -> new NotFoundException(WISH_NOTFOUND);
  }

  public static Supplier<BadRequestException> alreadyExists() {
    return () -> new BadRequestException(WISH_ALREADY_EXISTS);
  }
}
