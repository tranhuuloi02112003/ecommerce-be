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

    upsertImages(saved.getId(), request.imageUrls(), false);
    upsertColors(saved.getId(), request.colorIds(), false);
    upsertSizes(saved.getId(), request.sizeIds(), false);

    return productMapper.toResponse(saved, request.imageUrls());
  }

  @Transactional
  public ProductResponse update(UUID id, ProductRequest request) {
    ProductEntity product =
        productRepository.findById(id).orElseThrow(() -> ProductError.productNotFound().get());
    validateRefs(request);

    productMapper.updateFromRequest(request, product);
    ProductEntity saved = productRepository.save(product);

    upsertImages(saved.getId(), request.imageUrls(), true);
    upsertColors(saved.getId(), request.colorIds(), true);
    upsertSizes(saved.getId(), request.sizeIds(), true);

    return productMapper.toResponse(saved, request.imageUrls());
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

  @Transactional(readOnly = true)
  public ProductResponse getById(UUID productId) {
    ProductEntity product =
        productRepository.findById(productId).orElseThrow(ProductError.productNotFound());

    List<String> imageUrls = imageService.findUrlsByProductId(productId);
    List<UUID> colorIds = productColorRepository.findColorIdsByProductId(productId);
    List<UUID> sizeIds = productSizeRepository.findSizeIdByProductId(productId);

    return new ProductResponse(
        product.getId(),
        product.getName(),
        product.getDescription(),
        product.getPrice(),
        product.getCategoryId(),
        imageUrls,
        colorIds,
        sizeIds);
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
    long count = colorRepository.countByIdIn(colorIds);
    if (count != colorIds.size()) {
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

  private void upsertImages(UUID productId, List<String> imageUrls, boolean replace) {
    if (replace) {
      imageService.deleteByProductId(productId);
    }
    if (!CollectionUtils.isEmpty(imageUrls)) {
      List<ImageEntity> images = imageMapper.toEntity(imageUrls, productId);
      imageService.saveImages(images);
    }
  }

  private void upsertColors(UUID productId, List<UUID> colorIds, boolean replace) {
    if (replace) {
      productColorRepository.deleteByProductId(productId);
      productColorRepository.flush();
    }
    if (!CollectionUtils.isEmpty(colorIds)) {
      List<ProductColorEntity> list =
          colorIds.stream().map(cid -> new ProductColorEntity(null, productId, cid)).toList();
      productColorRepository.saveAll(list);
    }
  }

  private void upsertSizes(UUID productId, List<UUID> sizeIds, boolean replace) {
    if (replace) {
      productSizeRepository.deleteByProductId(productId);
      productColorRepository.flush();
    }
    if (!CollectionUtils.isEmpty(sizeIds)) {
      List<ProductSizeEntity> list =
          sizeIds.stream().map(sid -> new ProductSizeEntity(null, productId, sid)).toList();
      productSizeRepository.saveAll(list);
    }
  }
}
