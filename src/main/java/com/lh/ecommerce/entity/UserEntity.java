package com.lh.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseAuditEntity {
  private String password;
  private String email;
  private String firstName;
  private String lastName;
}
