package com.lh.ecommerce.service.color;

import com.lh.ecommerce.exception.Error;
import com.lh.ecommerce.exception.NotFoundException;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ColorError implements Error {
  COLOR_NOTFOUND("Color not found"),
  ;
  private final String message;

  @Override
  public String getName() {
    return name();
  }

  public static Supplier<NotFoundException> colorNotFound() {
    return () -> new NotFoundException(COLOR_NOTFOUND);
  }
}
