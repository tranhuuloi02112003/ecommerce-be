package com.lh.ecommerce.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class TokenResponse {
  private String accessToken;
  private String refreshToken;
}
