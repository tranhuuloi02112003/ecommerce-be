package com.lh.ecommerce.service.category;

import com.lh.ecommerce.dto.response.CategoryResponse;
import com.lh.ecommerce.dto.resquest.CategoryRequest;

public interface CategoryService {
  CategoryResponse create(CategoryRequest request);
}
