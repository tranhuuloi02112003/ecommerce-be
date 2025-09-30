package com.lh.ecommerce.mapper;

import com.lh.ecommerce.dto.response.PageBaseResponse;
import com.lh.ecommerce.dto.response.ProductBasicResponse;
import com.lh.ecommerce.dto.response.ProductHomeResponse;
import com.lh.ecommerce.dto.response.ProductResponse;
import com.lh.ecommerce.dto.resquest.ProductRequest;
import com.lh.ecommerce.entity.ProductEntity;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  ProductResponse toResponse(ProductEntity entity);

  @Mapping(target = "imageUrls", expression = "java(urls)")
  ProductResponse toResponse(ProductEntity entity, List<String> urls);

  ProductEntity toEntity(ProductRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  void toEntity(ProductRequest request, @MappingTarget ProductEntity entity);

  default PageBaseResponse<ProductBasicResponse> toPageResponse(Page<ProductEntity> pageData) {
    List<ProductBasicResponse> productBasicResponses =
        pageData.getContent().stream()
            .map(
                productEntity ->
                    ProductBasicResponse.builder()
                        .id(productEntity.getId())
                        .name(productEntity.getName())
                        .description(productEntity.getDescription())
                        .price(productEntity.getPrice())
                        .categoryName(productEntity.getCategory().getName())
                        .mainImage(productEntity.getMainImage().getUrl())
                        .build())
            .toList();

    return new PageBaseResponse<>(productBasicResponses, pageData);
  }

  default List<ProductHomeResponse> toProductHomeResponse(List<ProductEntity> productEntities) {
    final Instant threshold = Instant.now().minus(7, ChronoUnit.DAYS);
    return productEntities.stream()
        .map(
            productEntity ->
                ProductHomeResponse.builder()
                    .id(productEntity.getId())
                    .price(productEntity.getPrice())
                    .name(productEntity.getName())
                    .mainImage(productEntity.getMainImage().getUrl())
                    .isNew(
                        productEntity.getCreatedAt() != null
                            && productEntity.getCreatedAt().isAfter(threshold))
                    .isWish(productEntity.isWished())
                    .build())
        .toList();
  }
}
