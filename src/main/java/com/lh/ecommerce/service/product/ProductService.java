package com.lh.ecommerce.service.product;

import com.lh.ecommerce.adapter.UploadFileAdapter;
import com.lh.ecommerce.dto.response.*;
import com.lh.ecommerce.dto.resquest.BasePageRequest;
import com.lh.ecommerce.dto.resquest.ProductCriteriaRequest;
import com.lh.ecommerce.dto.resquest.ProductRequest;
import com.lh.ecommerce.entity.*;
import com.lh.ecommerce.mapper.ImageMapper;
import com.lh.ecommerce.mapper.ProductMapper;
import com.lh.ecommerce.repository.*;
import com.lh.ecommerce.service.category.CategoryError;
import com.lh.ecommerce.service.image.ImageError;
import com.lh.ecommerce.service.user.UserError;
import com.lh.ecommerce.utils.DateUtils;
import com.lh.ecommerce.utils.SecurityUtils;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ImageRepository imageRepository;
  private final UserRepository userRepository;
  private final WishRepository wishRepository;
  private final ProductMapper productMapper;
  private final ImageMapper imageMapper;
  private final UploadFileAdapter uploadFileAdapter;

  private static final int REQUIRED_IMAGES = 4;

  @Transactional
  public ProductResponse createProduct(ProductRequest request) {
    validateCategory(request.categoryId());
    validateImages(request.imageKeys());

    ProductEntity productEntity = productRepository.save(productMapper.toEntity(request));
    imageRepository.saveAll(imageMapper.toEntity(request.imageKeys(), productEntity.getId()));

    return productMapper.toResponse(productEntity, request.imageKeys(), uploadFileAdapter);
  }

  @Transactional
  public ProductResponse updateProduct(UUID id, ProductRequest request) {
    final var productEntity = validProduct(id);
    validateImages(request.imageKeys());

    productMapper.toEntity(request, productEntity);
    ProductEntity productSaved = productRepository.save(productEntity);

    imageRepository.deleteByProductId(productSaved.getId());
    imageRepository.flush();
    imageRepository.saveAll(imageMapper.toEntity(request.imageKeys(), productEntity.getId()));

    return productMapper.toResponse(productSaved, request.imageKeys(), uploadFileAdapter);
  }

  @Transactional
  public void deleteProduct(UUID id) {
    ProductEntity product = validProduct(id);
    productRepository.delete(product);
  }

  public PageBaseResponse<ProductBasicResponse> getAllProduct(ProductCriteriaRequest criteria) {
    Page<ProductEntity> pageData =
        productRepository.search(criteria.getSearch(), criteria.getPageable());

    return productMapper.toPageResponse(pageData, uploadFileAdapter);
  }

  public ProductResponse getById(UUID id) {
    ProductEntity product = validProduct(id);

    List<String> imageKeys = imageRepository.findKeysByProductId(id);
    List<ImageResponse> imageResponses =
        imageKeys.stream()
            .map(key -> new ImageResponse(key, uploadFileAdapter.getUrlS3(key)))
            .toList();

    return productMapper.toResponseWithImages(product, imageResponses);
  }

  public List<ProductHomeResponse> getLatestProducts() {
    UUID userId = SecurityUtils.getCurrentUserId();

    Instant threshold = DateUtils.sevenDaysAgo();
    List<ProductEntity> products =
        productRepository.getLatestProducts(userId, PageRequest.of(0, 15), threshold);

    return productMapper.toProductHomeResponse(products, uploadFileAdapter);
  }

  // Wishlist
  @Transactional
  public void createWishlistItem(UUID productId) {
    UUID userId = SecurityUtils.getCurrentUserId();

    if (!productRepository.existsById(productId)) {
      throw ProductError.productNotFound().get();
    }

    if (wishRepository.existsByUserIdAndProductId(userId, productId)) {
      throw ProductError.alreadyExistsWishListItem().get();
    }

    wishRepository.save(WishEntity.builder().userId(userId).productId(productId).build());
  }

  @Transactional
  public void deleteWishlistItem(UUID productId) {
    UUID userId = SecurityUtils.getCurrentUserId();
    userRepository.findById(userId).orElseThrow(UserError.userNotFound());

    if (!productRepository.existsById(productId)) {
      throw ProductError.productNotFound().get();
    }

    int deleted = wishRepository.deleteByUserIdAndProductId(userId, productId);
    if (deleted == 0) {
      throw ProductError.wishNotFound().get();
    }
  }

  public List<ProductHomeResponse> getUserWishlist() {
    UUID userId = SecurityUtils.getCurrentUserId();

    List<UUID> productIds = wishRepository.findProductIdsByUserId(userId);

    if (productIds.isEmpty()) {
      return List.of();
    }

    Instant threshold = DateUtils.sevenDaysAgo();
    List<ProductEntity> products = productRepository.findWishlistProducts(productIds, threshold);

    return productMapper.toProductHomeResponse(products, uploadFileAdapter);
  }

  public PageBaseResponse<ProductHomeResponse> getProductSByCategoryId(
      UUID categoryId, BasePageRequest pageRequest) {
    validateCategory(categoryId);

    UUID userId = SecurityUtils.getCurrentUserId();
    Instant threshold = DateUtils.sevenDaysAgo();

    Page<ProductEntity> pageData =
        productRepository.findByCategoryId(
            categoryId, userId, pageRequest.getPageable(), threshold);

    List<ProductHomeResponse> productHomeResponses =
        productMapper.toProductHomeResponse(pageData.getContent(), uploadFileAdapter);

    return new PageBaseResponse<>(productHomeResponses, pageData);
  }

  private void validateCategory(UUID categoryId) {
    if (!categoryRepository.existsById(categoryId)) {
      throw CategoryError.categoryNotFound().get();
    }
  }

  private void validateImages(List<String> imageKeys) {
    if (imageKeys.size() != REQUIRED_IMAGES) {
      throw ImageError.invalidImageCount().get();
    }
  }

  private ProductEntity validProduct(final UUID id) {
    return productRepository.findById(id).orElseThrow(ProductError.productNotFound());
  }
}
