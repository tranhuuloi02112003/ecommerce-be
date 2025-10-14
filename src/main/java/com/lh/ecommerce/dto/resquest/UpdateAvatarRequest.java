package com.lh.ecommerce.dto.resquest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAvatarRequest {
  @Size(max = 500, message = "Avatar key is too long")
  @NotBlank(message = "Avatar is required")
  private String avatarKey;
}
