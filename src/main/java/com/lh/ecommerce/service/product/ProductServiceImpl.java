package com.lh.ecommerce.service.product;

import com.lh.ecommerce.dto.response.ProductResponse;
import com.lh.ecommerce.dto.resquest.ProductRequest;
import com.lh.ecommerce.entity.ProductEntity;
import com.lh.ecommerce.mapper.ProductMapper;
import com.lh.ecommerce.repository.CategoryRepository;
import com.lh.ecommerce.repository.ProductRepository;
import com.lh.ecommerce.service.category.CategoryError;
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
  public ProductResponse create(ProductRequest request) {
    if (!categoryRepository.existsById(request.categoryId())) {
      throw CategoryError.categoryNotFound().get();
    }

    ProductEntity entity = productMapper.toEntity(request);
    UUID idUser = SecurityUtils.getCurrentUserId();
    entity.setUpdatedBy(idUser);
    entity.setCreatedBy(idUser);

    ProductEntity saved = productRepository.save(entity);
    return productMapper.toResponse(saved);
  }

  @Override
  public ProductResponse update(UUID id, ProductRequest request) {
    ProductEntity product =
        productRepository.findById(id).orElseThrow(() -> ProductError.productNotFound().get());

    productMapper.updateFromRequest(request, product);
    UUID idUser = SecurityUtils.getCurrentUserId();
    product.setUpdatedBy(idUser);

    ProductEntity saved = productRepository.save(product);
    return productMapper.toResponse(saved);
  }
}
