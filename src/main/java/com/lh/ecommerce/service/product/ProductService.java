package com.lh.ecommerce.service.product;

import com.lh.ecommerce.dto.response.PagedResponse;
import com.lh.ecommerce.dto.response.ProductListItemResponse;
import com.lh.ecommerce.dto.response.ProductResponse;
import com.lh.ecommerce.dto.resquest.ProductCriteriaRequest;
import com.lh.ecommerce.dto.resquest.ProductRequest;
import com.lh.ecommerce.entity.CategoryEntity;
import com.lh.ecommerce.entity.ImageEntity;
import com.lh.ecommerce.entity.ProductEntity;
import com.lh.ecommerce.mapper.ImageMapper;
import com.lh.ecommerce.mapper.ProductMapper;
import com.lh.ecommerce.repository.CategoryRepository;
import com.lh.ecommerce.repository.ProductRepository;
import com.lh.ecommerce.service.category.CategoryError;
import com.lh.ecommerce.service.image.ImageService;
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

@Service
@RequiredArgsConstructor
public class ProductService {
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ImageService imageService;
  private final ProductMapper productMapper;
  private final ImageMapper imageMapper;
  private final PageUtils pageUtils;

  @Transactional
  public ProductResponse create(ProductRequest request) {
    if (!categoryRepository.existsById(request.categoryId())) {
      throw CategoryError.categoryNotFound().get();
    }

    ProductEntity entity = productMapper.toEntity(request);
    ProductEntity saved = productRepository.save(entity);

    List<String> urls = request.imageUrls();
    if (urls != null && !urls.isEmpty()) {
      List<ImageEntity> images = imageMapper.toEntity(urls, saved.getId());
      imageService.saveImages(images);
      return productMapper.toResponse(saved, urls);
    }
    return productMapper.toResponse(saved, null);
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
    Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize());

    String search =
        (criteria.getSearch() == null || criteria.getSearch().isBlank())
            ? null
            : criteria.getSearch().trim().toLowerCase();

    Page<ProductEntity> pageData = productRepository.search(search, pageable);

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
}
