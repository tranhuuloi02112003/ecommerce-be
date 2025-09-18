package com.lh.ecommerce.service.size;

import com.lh.ecommerce.exception.Error;
import com.lh.ecommerce.exception.NotFoundException;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SizeError implements Error {
  SIZE_NOTFOUND("Size not found"),
  ;
  private final String message;

  @Override
  public String getName() {
    return name();
  }

  public static Supplier<NotFoundException> sizeNotFound() {
    return () -> new NotFoundException(SIZE_NOTFOUND);
  }
}
