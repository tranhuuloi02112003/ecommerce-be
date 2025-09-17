package com.lh.ecommerce.mapper;

import com.lh.ecommerce.entity.ImageEntity;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {
  ImageEntity toEntity(String url, UUID productId);

  default List<ImageEntity> toEntityList(List<String> urls, UUID productId) {
    return urls.stream().map(url -> toEntity(url, productId)).toList();
  }
}
