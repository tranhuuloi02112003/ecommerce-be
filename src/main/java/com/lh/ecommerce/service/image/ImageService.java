package com.lh.ecommerce.service.image;

import com.lh.ecommerce.entity.ImageEntity;
import com.lh.ecommerce.repository.ImageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {
  private final ImageRepository imageRepository;

  public void saveImages(List<ImageEntity> images) {
    imageRepository.saveAll(images);
  }
}
