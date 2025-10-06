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
public class UpdateInfoRequest {
  @NotBlank(message = "First name is required")
  @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
  private String firstName;

  @NotBlank(message = "Last name is required")
  @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
  private String lastName;

  @Size(max = 255, message = "Address must not exceed 255 characters")
  private String address;

  public void setFirstName(String firstName) {
    this.firstName = firstName != null ? firstName.trim() : null;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName != null ? lastName.trim() : null;
  }

  public void setAddress(String address) {
    this.address = address != null ? address.trim() : null;
  }
}
