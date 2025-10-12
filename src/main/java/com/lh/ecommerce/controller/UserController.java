package com.lh.ecommerce.controller;

import com.lh.ecommerce.dto.response.UserResponse;
import com.lh.ecommerce.dto.resquest.ChangePasswordRequest;
import com.lh.ecommerce.dto.resquest.UpdateAvatarRequest;
import com.lh.ecommerce.dto.resquest.UpdateInfoRequest;
import com.lh.ecommerce.service.user.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
  private final UserService userService;

  @GetMapping
  public List<UserResponse> getAllUser() {
    return userService.getAllUser();
  }

  @GetMapping("/me")
  public UserResponse getMe() {
    return userService.getCurrentUserProfile();
  }

  @PutMapping("/me")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateInfo(@Valid @RequestBody UpdateInfoRequest request) {
    userService.updateInfo(request);
  }

  @PutMapping("/me/avatar")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateAvatar(@Valid @RequestBody UpdateAvatarRequest request) {
    userService.updateAvatar(request.getAvatarKey());
  }

  @PostMapping("/me/change-password")
  public void changePassword(@Valid @RequestBody ChangePasswordRequest request) {
    userService.changePassword(request);
  }
}
