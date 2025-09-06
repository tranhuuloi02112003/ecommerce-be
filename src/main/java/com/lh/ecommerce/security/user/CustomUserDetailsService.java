package com.lh.ecommerce.security.user;

import com.lh.ecommerce.entity.UserEntity;
import com.lh.ecommerce.service.UserService;
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
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity userEntity = userService.getUserByUsername(username);
    return CustomUserPrincipal.builder()
        .id(userEntity.getId())
        .username(username)
        .password(userEntity.getPassword())
        .build();
  }
}
