package com.lh.ecommerce.mapper;

import com.lh.ecommerce.adapter.UploadFileAdapter;
import com.lh.ecommerce.dto.response.*;
import com.lh.ecommerce.dto.resquest.ProductRequest;
import com.lh.ecommerce.entity.ProductEntity;
import java.util.List;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(
    componentModel = "spring",
    uses = {ImageKeyUrlMapping.class})
public interface ProductMapper {
  ProductResponse toResponse(ProductEntity entity);

  @Mapping(target = "imageUrls", source = "imageKeys", qualifiedByName = "keysToUrls")
  ProductResponse toResponse(
      ProductEntity entity, List<String> imageKeys, @Context UploadFileAdapter uploadFileAdapter);

  @Mapping(target = "images", expression = "java(imageResponses)")
  ProductResponse toResponseWithImages(ProductEntity entity, List<ImageResponse> imageResponses);

  ProductEntity toEntity(ProductRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  void toEntity(ProductRequest request, @MappingTarget ProductEntity entity);

  default PageBaseResponse<ProductBasicResponse> toPageResponse(
      Page<ProductEntity> pageData, @Context UploadFileAdapter adapter) {
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
                        .mainImage(adapter.getUrlS3(productEntity.getMainImage().getKey()))
                        .build())
            .toList();
    return new PageBaseResponse<>(productBasicResponses, pageData);
  }

  @Mapping(target = "mainImage", source = "mainImage.key", qualifiedByName = "keyToUrl")
  @Mapping(target = "isNew", source = "new")
  @Mapping(target = "isWish", source = "wish")
  ProductHomeResponse toProductHomeResponse(
      ProductEntity entity, @Context UploadFileAdapter adapter);

  List<ProductHomeResponse> toProductHomeResponse(
      List<ProductEntity> entities, @Context UploadFileAdapter adapter);
}
