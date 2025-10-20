package com.lh.ecommerce.dto.identity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CertificationParam {
  String grant_type;
  String client_id;
  String client_secret;
  String scope;
}
