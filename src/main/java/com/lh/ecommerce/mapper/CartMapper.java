package com.lh.ecommerce.mapper;

import com.lh.ecommerce.dto.response.CartResponse;
import com.lh.ecommerce.entity.CartEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

  List<CartResponse> toCartsResponse(List<CartEntity> cartEntities);

  @Mapping(target = "price", source = "product.price")
  @Mapping(target = "productId", source = "product.id")
  @Mapping(target = "productName", source = "product.name")
  @Mapping(target = "productMainImage", source = "image.url")
  CartResponse toCartResponse(CartEntity cartEntity);
}
