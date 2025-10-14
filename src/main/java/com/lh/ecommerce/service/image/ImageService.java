package com.lh.ecommerce.service.image;

import com.lh.ecommerce.entity.ImageEntity;
import com.lh.ecommerce.repository.ImageRepository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class ImageService {
  private final ImageRepository imageRepository;

  public void saveImages(List<ImageEntity> images) {
    imageRepository.saveAll(images);
  }

  public Map<UUID, String> getMainImageKeys(Collection<UUID> ids) {
    if (CollectionUtils.isEmpty(ids)) {
      return Map.of();
    }
    List<ImageEntity> mainImages = imageRepository.findMainByProductIds(ids);
    return mainImages.stream()
        .collect(Collectors.toMap(ImageEntity::getProductId, ImageEntity::getKey));
  }

  public void deleteByProductId(UUID productId) {
    imageRepository.deleteByProductId(productId);
  }

  public List<String> findKeysByProductId(UUID productId) {
    return imageRepository.findKeysByProductId(productId);
  }
}
