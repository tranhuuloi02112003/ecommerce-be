package com.lh.ecommerce.controller;

import com.lh.ecommerce.dto.response.PageBaseResponse;
import com.lh.ecommerce.dto.response.ProductBasicResponse;
import com.lh.ecommerce.dto.response.ProductHomeResponse;
import com.lh.ecommerce.dto.response.ProductResponse;
import com.lh.ecommerce.dto.resquest.ProductCriteriaRequest;
import com.lh.ecommerce.dto.resquest.ProductRequest;
import com.lh.ecommerce.service.product.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
  private final ProductService productService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductResponse createProduct(@Valid @RequestBody ProductRequest productRequest) {
    return productService.createProduct(productRequest);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ProductResponse updateProduct(
      @PathVariable UUID id, @RequestBody ProductRequest productRequest) {
    return productService.updateProduct(id, productRequest);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteProduct(@PathVariable UUID id) {
    productService.deleteProduct(id);
  }

  @GetMapping
  public PageBaseResponse<ProductBasicResponse> getAllProduct(
      @Valid @ModelAttribute ProductCriteriaRequest criteria) {
    return productService.getAllProduct(criteria);
  }

  @GetMapping("/{id}")
  public ProductResponse getById(@PathVariable("id") UUID productId) {
    return productService.getById(productId);
  }

  @GetMapping("/explore")
  public List<ProductHomeResponse> getLatestProducts() {
    return productService.getLatestProducts();
  }

  // Wishlist
  @PostMapping("/{id}/wishlist")
  @ResponseStatus(HttpStatus.CREATED)
  public void createWishlistItem(@PathVariable UUID id) {
    productService.createWishlistItem(id);
  }

  @DeleteMapping("/{id}/wishlist")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteWishlistItem(@PathVariable UUID id) {
    productService.deleteWishlistItem(id);
  }

  @GetMapping("/wishlist")
  public List<ProductHomeResponse> getUserWishlist() {
    return productService.getUserWishlist();
  }
}
