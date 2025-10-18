package com.lh.ecommerce.service.order;

import com.lh.ecommerce.dto.response.OrderResponse;
import com.lh.ecommerce.dto.response.PageBaseResponse;
import com.lh.ecommerce.dto.resquest.OrderCriteriaRequest;
import com.lh.ecommerce.dto.resquest.OrderItemRequest;
import com.lh.ecommerce.dto.resquest.OrderRequest;
import com.lh.ecommerce.entity.OrderEntity;
import com.lh.ecommerce.entity.OrderItemEntity;
import com.lh.ecommerce.entity.ProductEntity;
import com.lh.ecommerce.mapper.OrderItemMapper;
import com.lh.ecommerce.mapper.OrderMapper;
import com.lh.ecommerce.repository.AddressRepository;
import com.lh.ecommerce.repository.OrderItemRepository;
import com.lh.ecommerce.repository.OrderRepository;
import com.lh.ecommerce.repository.ProductRepository;
import com.lh.ecommerce.service.address.AddressError;
import com.lh.ecommerce.service.product.ProductError;
import com.lh.ecommerce.utils.SecurityUtils;
import java.util.List;
import java.util.UUID;
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
  private final OrderItemRepository orderItemRepository;
  private final ProductRepository productRepository;
  private final AddressRepository addressRepository;
  private final OrderMapper orderMapper;
  private final OrderItemMapper orderItemMapper;

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

  @Transactional
  public void createOrder(OrderRequest request) {
    UUID userId = SecurityUtils.getCurrentUserId();

    validateAddressOwnership(request.getAddressId(), userId);
    validateAllProductsExist(request);

    decreaseStockForItems(request);

    OrderEntity savedOrder = saveOrder(request, userId);
    saveOrderItems(request, savedOrder.getId());
  }

  private void validateAddressOwnership(UUID addressId, UUID userId) {
    addressRepository
        .findByIdAndUserId(addressId, userId)
        .orElseThrow(AddressError.addressNotFound());
  }

  private void validateAllProductsExist(OrderRequest request) {
    List<UUID> productIds =
        request.getProductItems().stream().map(OrderItemRequest::getProductId).toList();
    List<ProductEntity> foundProducts = productRepository.findAllById(productIds);
    if (foundProducts.size() != request.getProductItems().size()) {
      throw ProductError.productNotFoundInList().get();
    }
  }

  private void decreaseStockForItems(OrderRequest request) {
    for (OrderItemRequest item : request.getProductItems()) {
      int updated = productRepository.decreaseQuantity(item.getProductId(), item.getQuantity());
      if (updated == 0) {
        throw OrderError.insufficientStock().get();
      }
    }
  }

  private OrderEntity saveOrder(OrderRequest request, UUID userId) {
    OrderEntity orderEntity = orderMapper.toEntity(request, userId);
    return orderRepository.save(orderEntity);
  }

  private void saveOrderItems(OrderRequest request, UUID orderId) {
    List<OrderItemEntity> orderItems = orderItemMapper.toEntity(request.getProductItems(), orderId);
    orderItemRepository.saveAll(orderItems);
  }
}
