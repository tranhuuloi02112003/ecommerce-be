package com.lh.ecommerce.controller;

import com.lh.ecommerce.entity.UserEntity;
import com.lh.ecommerce.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
  private final UserService userService;

  @GetMapping()
  public List<UserEntity> getAllUser() {
    return userService.getAllUser();
  }
}
