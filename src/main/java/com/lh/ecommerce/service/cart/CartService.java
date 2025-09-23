package com.lh.ecommerce.service.cart;

import com.lh.ecommerce.dto.response.CartResponse;
import com.lh.ecommerce.dto.resquest.CartRequest;
import com.lh.ecommerce.entity.CartEntity;
import com.lh.ecommerce.mapper.CartMapper;
import com.lh.ecommerce.repository.CartRepository;
import com.lh.ecommerce.repository.ProductRepository;
import com.lh.ecommerce.repository.UserRepository;
import com.lh.ecommerce.service.product.ProductError;
import com.lh.ecommerce.service.user.UserError;
import com.lh.ecommerce.utils.SecurityUtils;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
  private final CartRepository cartRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final CartMapper cartMapper;

  public List<CartResponse> findByUserId() {
    UUID userId = SecurityUtils.getCurrentUserId();
    userRepository.findById(userId).orElseThrow(UserError.userNotFound());
    return cartMapper.toResponse(cartRepository.findByUserId(userId));
  }

  @Transactional
  public List<CartResponse> update(CartRequest cartRequest) {
    UUID userId = SecurityUtils.getCurrentUserId();
    userRepository.findById(userId).orElseThrow(UserError.userNotFound());

    if (!cartRepository.existsByUserIdAndProductId(userId, cartRequest.getProductId())) {
      throw CartError.productNotInCart().get();
    }

    int updated =
        cartRepository.updateQuantityWithStock(
            userId, cartRequest.getProductId(), cartRequest.getQuantity());
    if (updated == 0) {
      throw CartError.quantityExceedsStock().get();
    }

    return cartMapper.toResponse(cartRepository.findByUserId(userId));
  }

  @Transactional
  public List<CartResponse> removeItem(UUID productId) {
    UUID userId = SecurityUtils.getCurrentUserId();
    userRepository.findById(userId).orElseThrow(UserError.userNotFound());

    int deleted = cartRepository.deleteOne(userId, productId);
    if (deleted == 0) {
      throw CartError.productNotInCart().get();
    }

    return cartMapper.toResponse(cartRepository.findByUserId(userId));
  }

  @Transactional
  public CartResponse addToCart(UUID productId) {
    UUID userId = SecurityUtils.getCurrentUserId();
    userRepository.findById(userId).orElseThrow(UserError.userNotFound());

    if (!productRepository.existsById(productId)) {
      throw ProductError.productNotFound().get();
    }

    CartEntity cart =
        cartRepository
            .findByUserIdAndProductId(userId, productId)
            .orElseGet(
                () -> CartEntity.builder().productId(productId).userId(userId).quantity(0).build());

    cart.setQuantity(cart.getQuantity() + 1);

    CartEntity saved = cartRepository.save(cart);
    return cartMapper.toResponse(saved);
  }
}
