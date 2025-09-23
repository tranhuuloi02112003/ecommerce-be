package com.lh.ecommerce.mapper;

import com.lh.ecommerce.dto.response.CartResponse;
import com.lh.ecommerce.entity.CartEntity;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
  CartResponse toResponse(CartEntity entity);

  default List<CartResponse> toResponse(List<CartEntity> cartEntities) {
    return cartEntities.stream()
        .map(
            cartEntity ->
                CartResponse.builder()
                    .quantity(cartEntity.getQuantity())
                    .price(cartEntity.getProductEntity().getPrice())
                    .productId(cartEntity.getProductEntity().getId())
                    .productName(cartEntity.getProductEntity().getName())
                    .productMainImage(cartEntity.getImageEntity().getUrl())
                    .build())
        .toList();
  }
}
