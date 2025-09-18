package com.lh.ecommerce.service.product;

import com.lh.ecommerce.dto.response.PagedResponse;
import com.lh.ecommerce.dto.response.ProductListItemResponse;
import com.lh.ecommerce.dto.response.ProductResponse;
import com.lh.ecommerce.dto.resquest.ProductCriteriaRequest;
import com.lh.ecommerce.dto.resquest.ProductRequest;
import com.lh.ecommerce.entity.*;
import com.lh.ecommerce.mapper.ImageMapper;
import com.lh.ecommerce.mapper.ProductMapper;
import com.lh.ecommerce.repository.*;
import com.lh.ecommerce.service.category.CategoryError;
import com.lh.ecommerce.service.color.ColorError;
import com.lh.ecommerce.service.image.ImageService;
import com.lh.ecommerce.service.size.SizeError;
import com.lh.ecommerce.utils.PageUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ProductService {
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ColorRepository colorRepository;
  private final SizeRepository sizeRepository;
  private final ProductColorRepository productColorRepository;
  private final ProductSizeRepository productSizeRepository;
  private final ImageService imageService;
  private final ProductMapper productMapper;
  private final ImageMapper imageMapper;
  private final PageUtils pageUtils;

  @Transactional
  public ProductResponse create(ProductRequest request) {
    validateRefs(request);

    ProductEntity entity = productMapper.toEntity(request);
    ProductEntity saved = productRepository.save(entity);

    List<ImageEntity> images = imageMapper.toEntity(request.imageUrls(), saved.getId());
    imageService.saveImages(images);

    if (!CollectionUtils.isEmpty(request.colorIds())) {
      List<ProductColorEntity> productColors =
          request.colorIds().stream()
              .map(sid -> new ProductColorEntity(null, saved.getId(), sid))
              .toList();
      productColorRepository.saveAll(productColors);
    }

    if (!CollectionUtils.isEmpty(request.sizeIds())) {
      List<ProductSizeEntity> productSizes =
          request.sizeIds().stream()
              .map(sid -> new ProductSizeEntity(null, saved.getId(), sid))
              .toList();
      productSizeRepository.saveAll(productSizes);
    }

    return productMapper.toResponse(saved, request.imageUrls());
  }

  @Transactional
  public ProductResponse update(UUID id, ProductRequest request) {
    ProductEntity product =
        productRepository.findById(id).orElseThrow(() -> ProductError.productNotFound().get());

    productMapper.updateFromRequest(request, product);

    ProductEntity saved = productRepository.save(product);

    List<String> urls = request.imageUrls();
    if (CollectionUtils.isEmpty(urls)) {
      imageService.deleteByProductId(saved.getId());
      return productMapper.toResponse(saved, null);
    }

    imageService.deleteByProductId(saved.getId());
    List<ImageEntity> images = imageMapper.toEntity(urls, saved.getId());
    imageService.saveImages(images);

    return productMapper.toResponse(saved, urls);
  }

  public void delete(UUID id) {
    ProductEntity product =
        productRepository.findById(id).orElseThrow(() -> ProductError.productNotFound().get());
    productRepository.delete(product);
  }

  @Transactional(readOnly = true)
  public PagedResponse<ProductListItemResponse> getAll(ProductCriteriaRequest criteria) {
    Pageable pageable = PageRequest.of(criteria.getPage() - 1, criteria.getSize());

    String search = criteria.getSearch();
    Page<ProductEntity> pageData;

    if (!StringUtils.hasText(search)) {
      pageData = productRepository.findAll(pageable);
    } else {
      pageData = productRepository.search(search.trim(), pageable);
    }

    if (pageData.isEmpty()) {
      return new PagedResponse<>(List.of(), pageUtils.toMeta(pageData));
    }

    List<ProductEntity> products = pageData.getContent();
    List<UUID> ids = products.stream().map(ProductEntity::getId).toList();
    List<UUID> categoryIds =
        products.stream().map(ProductEntity::getCategoryId).distinct().toList();

    Map<UUID, String> firstUrlByProductId = imageService.getMainImageUrls(ids);

    Map<UUID, String> categoryNameById =
        categoryRepository.findByIdIn(categoryIds).stream()
            .collect(Collectors.toMap(CategoryEntity::getId, CategoryEntity::getName));

    List<ProductListItemResponse> items =
        products.stream()
            .map(
                productEntity -> {
                  String url = firstUrlByProductId.get(productEntity.getId());
                  String categoryName = categoryNameById.get(productEntity.getCategoryId());
                  return new ProductListItemResponse(
                      productEntity.getId(),
                      productEntity.getName(),
                      productEntity.getDescription(),
                      productEntity.getPrice(),
                      categoryName,
                      url);
                })
            .toList();
    return new PagedResponse<>(items, pageUtils.toMeta(pageData));
  }

  private void validateRefs(ProductRequest request) {
    validateCategory(request.categoryId());
    validateColors(request.colorIds());
    validateSizes(request.sizeIds());
  }

  private void validateCategory(UUID categoryId) {
    if (!categoryRepository.existsById(categoryId)) {
      throw CategoryError.categoryNotFound().get();
    }
  }

  private void validateColors(List<UUID> colorIds) {
    if (CollectionUtils.isEmpty(colorIds)) return;
    int distinctSize = new HashSet<>(colorIds).size();
    long count = colorRepository.countByIdIn(colorIds);
    if (count != distinctSize) {
      throw ColorError.colorNotFound().get();
    }
  }

  private void validateSizes(List<UUID> sizeIds) {
    if (CollectionUtils.isEmpty(sizeIds)) return;
    int distinctSize = new HashSet<>(sizeIds).size();
    long count = sizeRepository.countByIdIn(sizeIds);
    if (count != distinctSize) {
      throw SizeError.sizeNotFound().get();
    }
  }
}
