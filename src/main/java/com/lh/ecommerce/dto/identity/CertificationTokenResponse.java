package com.lh.ecommerce.dto.identity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CertificationTokenResponse {
  String accessToken;
  String expiresIn;
  String refreshExpiresIn;
  String tokenType;
  String idToken;
  String scope;
}
