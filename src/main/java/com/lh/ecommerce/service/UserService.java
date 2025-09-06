package com.lh.ecommerce.service;

import com.lh.ecommerce.dto.response.User;
import com.lh.ecommerce.entity.UserEntity;
import com.lh.ecommerce.mapper.UserMapper;
import com.lh.ecommerce.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public List<User> getAllUser() {
    List<UserEntity> userEntities = userRepository.findAll();
    return userMapper.toDtoList(userEntities);
  }

  public UserEntity getUserByUsername(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(username));
  }
}
