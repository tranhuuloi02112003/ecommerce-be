package com.lh.ecommerce.service.cart;

import com.lh.ecommerce.adapter.UploadFileAdapter;
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
  private final UploadFileAdapter uploadFileAdapter;

  public List<CartResponse> findByUserId() {
    UUID userId = SecurityUtils.getCurrentUserId();
    userRepository.findById(userId).orElseThrow(UserError.userNotFound());
    return cartMapper.toCartsResponse(cartRepository.findByUserId(userId), uploadFileAdapter);
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

    return cartMapper.toCartsResponse(cartRepository.findByUserId(userId), uploadFileAdapter);
  }

  @Transactional
  public List<CartResponse> removeCartByProductId(UUID productId) {
    UUID userId = SecurityUtils.getCurrentUserId();
    userRepository.findById(userId).orElseThrow(UserError.userNotFound());

    int deleted = cartRepository.deleteByUserIdAndProductId(userId, productId);
    if (deleted == 0) {
      throw CartError.productNotInCart().get();
    }

    return cartMapper.toCartsResponse(cartRepository.findByUserId(userId), uploadFileAdapter);
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
            .map(
                c -> {
                  c.setQuantity(c.getQuantity() + 1);
                  return c;
                })
            .orElseGet(
                () -> CartEntity.builder().productId(productId).userId(userId).quantity(1).build());

    CartEntity saved = cartRepository.save(cart);
    return cartMapper.toCartResponse(saved, uploadFileAdapter);
  }
}
