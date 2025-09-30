package com.lh.ecommerce.service.wish;

import com.lh.ecommerce.entity.WishEntity;
import com.lh.ecommerce.repository.ProductRepository;
import com.lh.ecommerce.repository.UserRepository;
import com.lh.ecommerce.repository.WishRepository;
import com.lh.ecommerce.service.product.ProductError;
import com.lh.ecommerce.service.user.UserError;
import com.lh.ecommerce.utils.SecurityUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishService {
  private final WishRepository wishRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;

  @Transactional
  public void addWish(UUID productId) {
    UUID userId = SecurityUtils.getCurrentUserId();
    userRepository.findById(userId).orElseThrow(UserError.userNotFound());

    if (!productRepository.existsById(productId)) {
      throw ProductError.productNotFound().get();
    }

    if (wishRepository.existsByUserIdAndProductId(userId, productId)) {
      throw WishError.alreadyExists().get();
    }

    WishEntity saved =
        wishRepository.save(WishEntity.builder().userId(userId).productId(productId).build());
    wishRepository.save(saved);
  }

  @Transactional
  public void removeWish(UUID productId) {
    UUID userId = SecurityUtils.getCurrentUserId();
    userRepository.findById(userId).orElseThrow(UserError.userNotFound());

    if (!productRepository.existsById(productId)) {
      throw ProductError.productNotFound().get();
    }

    int deleted = wishRepository.deleteByUserIdAndProductId(userId, productId);
    if (deleted == 0) {
      throw WishError.wishNotFound().get();
    }
  }
}
