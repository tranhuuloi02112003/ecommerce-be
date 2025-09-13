package com.lh.ecommerce.service.product;

import com.lh.ecommerce.dto.response.ProductResponse;
import com.lh.ecommerce.dto.resquest.ProductCreateRequest;

public interface ProductService {
  ProductResponse create(ProductCreateRequest request);
}
