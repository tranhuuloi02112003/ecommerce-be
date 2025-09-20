package com.lh.ecommerce.service.product;

import com.lh.ecommerce.dto.response.PageBaseResponse;
import com.lh.ecommerce.dto.response.ProductBasicResponse;
import com.lh.ecommerce.dto.response.ProductResponse;
import com.lh.ecommerce.dto.resquest.ProductCriteriaRequest;
import com.lh.ecommerce.dto.resquest.ProductRequest;
import com.lh.ecommerce.entity.*;
import com.lh.ecommerce.mapper.ImageMapper;
import com.lh.ecommerce.mapper.ProductColorMapper;
import com.lh.ecommerce.mapper.ProductMapper;
import com.lh.ecommerce.mapper.ProductSizeMapper;
import com.lh.ecommerce.repository.*;
import com.lh.ecommerce.service.category.CategoryError;
import com.lh.ecommerce.service.color.ColorError;
import com.lh.ecommerce.service.size.SizeError;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ColorRepository colorRepository;
  private final SizeRepository sizeRepository;
  private final ProductColorRepository productColorRepository;
  private final ProductSizeRepository productSizeRepository;
  private final ImageRepository imageRepository;
  private final ProductMapper productMapper;
  private final ImageMapper imageMapper;
  private final ProductColorMapper productColorMapper;
  private final ProductSizeMapper productSizeMapper;

  @Transactional
  public ProductResponse create(ProductRequest request) {
    validateCategory(request.categoryId());
    validateColors(request.colorIds());
    validateSizes(request.sizeIds());

    ProductEntity productEntity = productRepository.save(productMapper.toEntity(request));

    insertImages(productEntity.getId(), request.imageUrls());
    insertColors(productEntity.getId(), request.colorIds());
    insertSizes(productEntity.getId(), request.sizeIds());

    return productMapper.toResponse(productEntity, request.imageUrls());
  }

  //  @Transactional
  //  public ProductResponse update(UUID id, ProductRequest request) {
  //    final var productEntity = validProduct(id);
  //    validateRefs(request);
  //
  //    productMapper.toEntity(request, productEntity);
  //    ProductEntity saved = productRepository.save(productEntity);
  //
  //    insertImages(saved.getId(), request.imageUrls(), true);
  //    //    upsertColors(saved.getId(), request.colorIds(), true);
  //    insertSizes(saved.getId(), request.sizeIds(), true);
  //
  //    return productMapper.toResponse(saved, request.imageUrls());
  //  }

  @Transactional
  public void delete(UUID id) {
    ProductEntity product = validProduct(id);
    productRepository.delete(product);
  }

  private ProductEntity validProduct(final UUID id) {
    return productRepository.findById(id).orElseThrow(ProductError.productNotFound());
  }

  public PageBaseResponse<ProductBasicResponse> getAll(ProductCriteriaRequest criteria) {
    Page<ProductEntity> pageData =
        productRepository.search(criteria.getSearch(), criteria.getPageable());

    return productMapper.toPageResponse(pageData);
  }

  public ProductResponse getById(UUID id) {
    ProductEntity product = validProduct(id);

    List<String> imageUrls = imageRepository.findUrlsByProductId(id);
    List<UUID> colorIds = productColorRepository.findColorIdsByProductId(id);
    List<UUID> sizeIds = productSizeRepository.findSizeIdByProductId(id);

    return productMapper.toResponse(product, imageUrls, colorIds, sizeIds);
  }

  private void validateCategory(UUID categoryId) {
    if (!categoryRepository.existsById(categoryId)) {
      throw CategoryError.categoryNotFound().get();
    }
  }

  private void validateColors(List<UUID> colorIds) {
    if (CollectionUtils.isEmpty(colorIds)) return;

    long countColors = colorRepository.countByIdIn(colorIds);
    if (countColors != colorIds.size()) {
      throw ColorError.colorNotFound().get();
    }
  }

  private void validateSizes(List<UUID> sizeIds) {
    if (CollectionUtils.isEmpty(sizeIds)) return;

    long count = sizeRepository.countByIdIn(sizeIds);
    if (count != sizeIds.size()) {
      throw SizeError.sizeNotFound().get();
    }
  }

  private void insertImages(UUID productId, List<String> imageUrls) {
    if (CollectionUtils.isEmpty(imageUrls)) {
      return;
    }
    imageRepository.saveAll(imageMapper.toEntity(imageUrls, productId));
  }

  private void insertColors(UUID productId, List<UUID> colorIds) {
    if (CollectionUtils.isEmpty(colorIds)) {
      return;
    }
    productColorRepository.saveAll(productColorMapper.toEntity(colorIds, productId));
  }

  private void insertSizes(UUID productId, List<UUID> sizeIds) {
    if (CollectionUtils.isEmpty(sizeIds)) {
      return;
    }
    productSizeRepository.saveAll(productSizeMapper.toEntity(sizeIds, productId));
  }
}
