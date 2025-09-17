package com.lh.ecommerce.service.category;

import com.lh.ecommerce.dto.response.CategoryResponse;
import com.lh.ecommerce.dto.resquest.CategoryRequest;
import com.lh.ecommerce.entity.CategoryEntity;
import com.lh.ecommerce.mapper.CategoryMapper;
import com.lh.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  @Override
  public CategoryResponse create(CategoryRequest request) {
    CategoryEntity entity = categoryMapper.toEntity(request);
    CategoryEntity saved = categoryRepository.save(entity);
    return categoryMapper.toCategoryResponse(saved);
  }
}
