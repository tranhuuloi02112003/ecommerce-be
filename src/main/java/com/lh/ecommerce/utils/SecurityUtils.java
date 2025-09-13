package com.lh.ecommerce.utils;

import com.lh.ecommerce.security.user.CustomUserPrincipal;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
  public static UUID getCurrentUserId() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return ((CustomUserPrincipal) principal).getId();
  }
}
