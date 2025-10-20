package com.lh.ecommerce.dto.resquest;

import com.lh.ecommerce.enums.OrderStatus;
import com.lh.ecommerce.enums.PaymentStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class OrderCriteriaRequest extends BasePageRequest {
  private String search;
  private OrderStatus orderStatus;
  private PaymentStatus paymentStatus;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate endDate;

  public String getSearch() {
    return StringUtils.trimToEmpty(search);
  }

  public Instant getStartDate() {
    return startDate != null ? startDate.atStartOfDay(ZoneOffset.UTC).toInstant() : null;
  }

  public Instant getEndDate() {
    return endDate != null ? endDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant() : null;
  }
}
