package com.lh.ecommerce.dto.resquest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {

  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Phone is required")
  @Pattern(
      regexp = "^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$",
      message = "Please provide a valid phone number")
  private String phone;

  @NotBlank(message = "Street address is required")
  private String streetAddress;

  private String city;
}
