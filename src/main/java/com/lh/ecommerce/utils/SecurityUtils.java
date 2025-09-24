package com.lh.ecommerce.utils;

import com.lh.ecommerce.security.user.CustomUserPrincipal;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
  public static UUID getCurrentUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated()) return null;

    Object principal = auth.getPrincipal();
    if (principal instanceof CustomUserPrincipal p) {
      return p.getId();
    }
    return null;
  }
}
