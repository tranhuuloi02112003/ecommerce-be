package com.lh.ecommerce.mapper;

import com.lh.ecommerce.dto.response.CategoryResponse;
import com.lh.ecommerce.dto.resquest.CategoryRequest;
import com.lh.ecommerce.entity.CategoryEntity;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  CategoryResponse toResponse(CategoryEntity entity);

  List<CategoryResponse> toResponse(List<CategoryEntity> entities);

  CategoryEntity toEntity(CategoryRequest request);
}
