package com.lh.ecommerce.controller;

import com.lh.ecommerce.service.wish.WishService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishes")
public class WishController {
  private final WishService wishService;

  @PostMapping("/{productId}")
  @ResponseStatus(HttpStatus.CREATED)
  public void addWish(@PathVariable UUID productId) {
    wishService.addWish(productId);
  }

  @DeleteMapping("/{productId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeWish(@PathVariable UUID productId) {
    wishService.removeWish(productId);
  }
}
