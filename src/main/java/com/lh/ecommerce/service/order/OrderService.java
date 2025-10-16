package com.lh.ecommerce.service.order;

import com.lh.ecommerce.dto.response.OrderResponse;
import com.lh.ecommerce.dto.response.PageBaseResponse;
import com.lh.ecommerce.dto.resquest.OrderCriteriaRequest;
import com.lh.ecommerce.entity.OrderEntity;
import com.lh.ecommerce.mapper.OrderMapper;
import com.lh.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;

  public PageBaseResponse<OrderResponse> getAll(OrderCriteriaRequest criteria) {
    Page<OrderEntity> pageData =
        orderRepository.searchWithFilters(
            criteria.getSearch(),
            criteria.getOrderStatus(),
            criteria.getPaymentStatus(),
            criteria.getStartDate(),
            criteria.getEndDate(),
            criteria.getPageable());
    return orderMapper.toPageResponse(pageData);
  }
}
