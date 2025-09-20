package com.lh.ecommerce.service.image;

import com.lh.ecommerce.exception.BadRequestException;
import com.lh.ecommerce.exception.Error;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageError implements Error {
  INVALID_IMAGE_COUNT("Product must have 4 images"),
  ;
  private final String message;

  @Override
  public String getName() {
    return name();
  }

  public static Supplier<BadRequestException> invalidImageCount() {
    return () -> new BadRequestException(INVALID_IMAGE_COUNT);
  }
}
