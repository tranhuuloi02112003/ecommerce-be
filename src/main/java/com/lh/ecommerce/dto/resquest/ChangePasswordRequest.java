package com.lh.ecommerce.dto.resquest;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
  @Size(min = 6, max = 64, message = "Current password must be 6-64 characters")
  private String currentPassword;

  @Size(min = 6, max = 64, message = "New password must be 6-64 characters")
  private String newPassword;
}
