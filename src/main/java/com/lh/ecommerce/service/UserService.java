package com.lh.ecommerce.service;

import com.lh.ecommerce.entity.UserEntity;
import com.lh.ecommerce.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public List<UserEntity> getAllUser() {
    return userRepository.findAll();
  }
}
