package com.lh.ecommerce.service.user;

import com.lh.ecommerce.dto.response.UserResponse;
import com.lh.ecommerce.entity.UserEntity;
import com.lh.ecommerce.mapper.UserMapper;
import com.lh.ecommerce.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public List<UserResponse> getAllUser() {
    List<UserEntity> userEntities = userRepository.findAll();
    return userMapper.toResponse(userEntities);
  }

  public UserEntity getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(UserError.userNotFound());
  }
}
