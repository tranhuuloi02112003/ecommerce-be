package com.lh.ecommerce.service.user;

import com.lh.ecommerce.adapter.UploadFileAdapter;
import com.lh.ecommerce.dto.response.UserResponse;
import com.lh.ecommerce.dto.resquest.ChangePasswordRequest;
import com.lh.ecommerce.dto.resquest.UpdateInfoRequest;
import com.lh.ecommerce.entity.UserEntity;
import com.lh.ecommerce.mapper.UserMapper;
import com.lh.ecommerce.repository.UserRepository;
import com.lh.ecommerce.utils.SecurityUtils;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final UploadFileAdapter uploadFileAdapter;

  public List<UserResponse> getAllUser() {
    List<UserEntity> userEntities = userRepository.findAll();
    return userMapper.toResponse(userEntities);
  }

  public UserEntity getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(UserError.userNotFound());
  }

  public UserResponse getCurrentUserProfile() {
    UUID userId = SecurityUtils.getCurrentUserId();
    UserEntity userEntity = userRepository.findById(userId).orElseThrow(UserError.userNotFound());

    return userMapper.toResponse(userEntity, uploadFileAdapter);
  }

  @Transactional
  public void updateInfo(UpdateInfoRequest req) {
    UUID userId = SecurityUtils.getCurrentUserId();
    UserEntity user = userRepository.findById(userId).orElseThrow(UserError.userNotFound());

    userMapper.updateEntity(req, user);
    userRepository.save(user);
  }

  @Transactional
  public void changePassword(ChangePasswordRequest req) {
    UUID userId = SecurityUtils.getCurrentUserId();
    UserEntity user = userRepository.findById(userId).orElseThrow(UserError.userNotFound());

    boolean matches = passwordEncoder.matches(req.getCurrentPassword(), user.getPassword());
    if (!matches) throw UserError.invalidCurrentPassword().get();

    user.setPassword(passwordEncoder.encode(req.getNewPassword()));
    userRepository.save(user);
  }

  @Transactional
  public void updateAvatar(String avatarKey) {
    UUID userId = SecurityUtils.getCurrentUserId();
    UserEntity user = userRepository.findById(userId).orElseThrow(UserError.userNotFound());
    user.setAvatarKey(avatarKey.trim());
    userRepository.save(user);
  }
}
