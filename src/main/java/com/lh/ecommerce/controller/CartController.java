package com.lh.ecommerce.controller;

import com.lh.ecommerce.dto.response.CartResponse;
import com.lh.ecommerce.dto.resquest.CartRequest;
import com.lh.ecommerce.service.cart.CartService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {
  private final CartService cartService;

  @GetMapping
  public List<CartResponse> findByUserId() {
    return cartService.findByUserId();
  }

  @PutMapping
  public List<CartResponse> update(@Valid @RequestBody CartRequest cartRequest) {
    return cartService.update(cartRequest);
  }

  @DeleteMapping("/{id}")
  public List<CartResponse> delete(@PathVariable("id") UUID productId) {
    return cartService.removeCartByProductId(productId);
  }

  @PostMapping("/{productId}")
  @ResponseStatus(HttpStatus.CREATED)
  public CartResponse addToCart(@PathVariable UUID productId) {
    return cartService.addToCart(productId);
  }
}
