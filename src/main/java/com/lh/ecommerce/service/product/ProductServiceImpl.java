package com.lh.ecommerce.service.product;

import com.lh.ecommerce.dto.response.ProductResponse;
import com.lh.ecommerce.dto.resquest.ProductCreateRequest;
import com.lh.ecommerce.entity.ProductEntity;
import com.lh.ecommerce.mapper.ProductMapper;
import com.lh.ecommerce.repository.CategoryRepository;
import com.lh.ecommerce.repository.ProductRepository;
import com.lh.ecommerce.utils.SecurityUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ProductMapper productMapper;

  @Override
  public ProductResponse create(ProductCreateRequest request) {
    if (!categoryRepository.existsById(request.categoryId())) {
      throw new RuntimeException("Category not found");
    }

    ProductEntity entity = productMapper.toProductEntity(request);
    UUID idUser = SecurityUtils.getCurrentUserId();
    entity.setUpdatedBy(idUser);
    entity.setCreatedBy(idUser);

    ProductEntity saved = productRepository.save(entity);
    return productMapper.toProductResponse(saved);
  }
}
