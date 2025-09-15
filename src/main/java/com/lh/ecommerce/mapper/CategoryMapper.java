package com.lh.ecommerce.mapper;

import com.lh.ecommerce.dto.response.CategoryResponse;
import com.lh.ecommerce.dto.resquest.CategoryRequest;
import com.lh.ecommerce.entity.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  CategoryResponse toCategoryResponse(CategoryEntity entity);

  CategoryEntity toEntity(CategoryRequest request);
}
