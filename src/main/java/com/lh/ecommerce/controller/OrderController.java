package com.lh.ecommerce.controller;

import com.lh.ecommerce.dto.response.OrderResponse;
import com.lh.ecommerce.dto.response.PageBaseResponse;
import com.lh.ecommerce.dto.resquest.OrderCriteriaRequest;
import com.lh.ecommerce.dto.resquest.OrderRequest;
import com.lh.ecommerce.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @GetMapping
  public PageBaseResponse<OrderResponse> getAll(
      @Valid @ModelAttribute OrderCriteriaRequest criteria) {
    return orderService.getAll(criteria);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createOrder(@Valid @RequestBody OrderRequest request) {
    orderService.createOrder(request);
  }
}
