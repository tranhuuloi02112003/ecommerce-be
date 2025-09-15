package com.lh.ecommerce.service.product;

import com.lh.ecommerce.dto.response.ProductResponse;
import com.lh.ecommerce.dto.resquest.ProductRequest;
import com.lh.ecommerce.entity.ImageEntity;
import com.lh.ecommerce.entity.ProductEntity;
import com.lh.ecommerce.mapper.ImageMapper;
import com.lh.ecommerce.mapper.ProductMapper;
import com.lh.ecommerce.repository.CategoryRepository;
import com.lh.ecommerce.repository.ImageRepository;
import com.lh.ecommerce.repository.ProductRepository;
import com.lh.ecommerce.service.category.CategoryError;
import com.lh.ecommerce.utils.SecurityUtils;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ImageRepository imageRepository;
  private final ProductMapper productMapper;
  private final ImageMapper imageMapper;

  @Transactional
  @Override
  public ProductResponse create(ProductRequest request) {
    if (!categoryRepository.existsById(request.categoryId())) {
      throw CategoryError.categoryNotFound().get();
    }

    ProductEntity entity = productMapper.toEntity(request);
    UUID idUser = SecurityUtils.getCurrentUserId();
    entity.setUpdatedBy(idUser);
    entity.setCreatedBy(idUser);
    ProductEntity saved = productRepository.save(entity);

    List<String> urls = request.imageUrls();
    if (urls != null && !urls.isEmpty()) {
      List<ImageEntity> images = imageMapper.toEntityList(urls, saved.getId());
      imageRepository.saveAll(images);
      return productMapper.toResponse(saved, urls);
    }
    return productMapper.toResponse(saved, null);
  }

  @Transactional
  @Override
  public ProductResponse update(UUID id, ProductRequest request) {
    ProductEntity product =
        productRepository.findById(id).orElseThrow(() -> ProductError.productNotFound().get());

    productMapper.updateFromRequest(request, product);
    UUID idUser = SecurityUtils.getCurrentUserId();
    product.setUpdatedBy(idUser);

    ProductEntity saved = productRepository.save(product);

    List<String> urls = request.imageUrls();
    if (urls == null || urls.isEmpty()) {
      return productMapper.toResponse(saved);
    }

    imageRepository.deleteByProductId(saved.getId());
    imageRepository.saveAll(imageMapper.toEntityList(urls, saved.getId()));

    return productMapper.toResponse(saved, urls);
  }

  @Override
  public void delete(UUID id) {
    ProductEntity product =
        productRepository.findById(id).orElseThrow(() -> ProductError.productNotFound().get());
    productRepository.delete(product);
  }
}
