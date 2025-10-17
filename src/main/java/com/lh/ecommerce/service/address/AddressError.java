package com.lh.ecommerce.service.address;

import com.lh.ecommerce.exception.BadRequestException;
import com.lh.ecommerce.exception.Error;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AddressError implements Error {
  ADDRESS_NOT_FOUND("Address not found"),
  ADDRESS_NOT_BELONG_TO_USER("Address does not belong to current user"),
  CANNOT_DELETE_DEFAULT("Cannot delete the default address"),
  ADDRESS_LIMIT_REACHED("Cannot add more than 4 addresses"),
  ;

  private final String message;

  @Override
  public String getName() {
    return name();
  }

  public static Supplier<BadRequestException> addressNotFound() {
    return () -> new BadRequestException(ADDRESS_NOT_FOUND);
  }

  public static Supplier<BadRequestException> addressNotBelongToUser() {

    return () -> new BadRequestException(ADDRESS_NOT_BELONG_TO_USER);
  }

  public static Supplier<BadRequestException> cannotDeleteDefaultAddress() {
    return () -> new BadRequestException(CANNOT_DELETE_DEFAULT);
  }

  public static Supplier<BadRequestException> addressLimitReached() {
    return () -> new BadRequestException(ADDRESS_LIMIT_REACHED);
  }
}
