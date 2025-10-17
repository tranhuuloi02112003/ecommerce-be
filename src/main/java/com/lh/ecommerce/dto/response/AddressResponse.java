package com.lh.ecommerce.dto.response;

import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {
  private UUID id;
  private String name;
  private String phone;
  private String streetAddress;
  private String city;
  private boolean defaultAddress;
}
