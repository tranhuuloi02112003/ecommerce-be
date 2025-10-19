package com.lh.ecommerce.dto.resquest;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerificationRequest {
  private String to;
  private String token;
  private String fullName;
}
