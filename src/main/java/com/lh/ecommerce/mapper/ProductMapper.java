package com.lh.ecommerce.mapper;

import com.lh.ecommerce.dto.response.ProductResponse;
import com.lh.ecommerce.dto.resquest.ProductRequest;
import com.lh.ecommerce.entity.ProductEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  ProductResponse toResponse(ProductEntity entity);

  @Mapping(target = "imageUrls", expression = "java(urls)")
  ProductResponse toResponse(ProductEntity entity, List<String> urls);

  ProductEntity toEntity(ProductRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  void updateFromRequest(ProductRequest request, @MappingTarget ProductEntity entity);
}
