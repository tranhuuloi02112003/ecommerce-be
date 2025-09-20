package com.lh.ecommerce.mapper;

import com.lh.ecommerce.entity.ProductColorEntity;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductColorMapper {
  default List<ProductColorEntity> toEntity(List<UUID> colorIds, UUID productId) {
    return colorIds.stream()
        .map(colorId -> ProductColorEntity.builder().productId(productId).colorId(colorId).build())
        .toList();
  }
}
