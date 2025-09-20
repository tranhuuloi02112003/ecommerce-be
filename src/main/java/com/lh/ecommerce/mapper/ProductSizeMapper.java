package com.lh.ecommerce.mapper;

import com.lh.ecommerce.entity.ProductSizeEntity;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductSizeMapper {
  default List<ProductSizeEntity> toEntity(List<UUID> sizeIds, UUID productId) {
    return sizeIds.stream()
        .map(sizeId -> ProductSizeEntity.builder().productId(productId).sizeId(sizeId).build())
        .toList();
  }
}
