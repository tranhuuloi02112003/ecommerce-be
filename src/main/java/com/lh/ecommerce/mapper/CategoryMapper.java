package com.lh.ecommerce.mapper;

import com.lh.ecommerce.dto.response.CategoryResponse;
import com.lh.ecommerce.dto.resquest.CategoryRequest;
import com.lh.ecommerce.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  CategoryResponse toCategoryResponse(CategoryEntity entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  CategoryEntity toNewCategoryEntity(CategoryRequest request);
}
