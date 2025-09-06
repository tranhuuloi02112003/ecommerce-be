package com.lh.ecommerce.dto.response;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
  private UUID id;
  private String username;
  private Instant createdAt;
  private Instant updatedAt;
}
