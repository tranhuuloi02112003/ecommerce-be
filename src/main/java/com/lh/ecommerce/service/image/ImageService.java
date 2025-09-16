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

@Service
@RequiredArgsConstructor
public class ImageService {
  private final ImageRepository imageRepository;

  public void saveImages(List<ImageEntity> images) {
    imageRepository.saveAll(images);
  }

  public Map<UUID, String> getFirstImageUrls(Collection<UUID> ids) {
    if (ids == null || ids.isEmpty()) return Map.of();
    List<ImageEntity> firstImages = imageRepository.findFirstByProductIds(ids);
    return firstImages.stream()
        .collect(Collectors.toMap(ImageEntity::getProductId, ImageEntity::getUrl));
  }

  public void deleteByProductId(UUID productId) {
    imageRepository.deleteByProductId(productId);
  }
}
