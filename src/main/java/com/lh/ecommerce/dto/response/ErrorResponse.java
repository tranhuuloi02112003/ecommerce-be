package com.lh.ecommerce.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
  private int status;
  private String message;
  private String path;
  private LocalDateTime timestamp;
}
