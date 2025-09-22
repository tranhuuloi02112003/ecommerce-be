package com.lh.ecommerce.security.user;

import com.lh.ecommerce.entity.UserEntity;
import com.lh.ecommerce.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    UserEntity userEntity = userService.getUserByEmail(email);

    return CustomUserPrincipal.builder()
        .id(userEntity.getId())
        .email(email)
        .password(userEntity.getPassword())
        .build();
  }
}
