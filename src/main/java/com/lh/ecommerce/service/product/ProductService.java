package com.lh.ecommerce.service.product;

import com.lh.ecommerce.dto.response.ProductResponse;
import com.lh.ecommerce.dto.resquest.ProductRequest;
import java.util.UUID;

public interface ProductService {
  ProductResponse create(ProductRequest request);

  ProductResponse update(UUID id, ProductRequest request);
}
