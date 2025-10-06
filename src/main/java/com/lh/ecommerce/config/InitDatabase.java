package com.lh.ecommerce.config;

import com.lh.ecommerce.entity.UserEntity;
import com.lh.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDatabase implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    boolean hasUser = userRepository.existsByEmail("admin@gmail.com");

    if (hasUser) {
      return;
    }

    userRepository.save(
        UserEntity.builder()
            .email("admin@gmail.com")
            .password(passwordEncoder.encode("admin123"))
            .firstName("admin")
            .lastName("admin")
            .build());
  }
}
