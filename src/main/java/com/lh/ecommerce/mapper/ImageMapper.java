package com.lh.ecommerce.mapper;

import com.lh.ecommerce.entity.ImageEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {

  default List<ImageEntity> toEntity(List<String> keys, UUID productId) {
    List<ImageEntity> images = new ArrayList<>();
    for (int i = 0; i < keys.size(); i++) {
      ImageEntity image = new ImageEntity();
      image.setProductId(productId);
      image.setKey(keys.get(i));
      image.setMain(i == 0);
      images.add(image);
    }
    return images;
  }
}
