package com.lh.ecommerce.mapper;

import com.lh.ecommerce.dto.resquest.OrderItemRequest;
import com.lh.ecommerce.entity.OrderItemEntity;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

  @Mapping(target = "orderId", expression = "java(orderId)")
  OrderItemEntity toEntity(OrderItemRequest request, UUID orderId);

  default List<OrderItemEntity> toEntity(List<OrderItemRequest> requests, UUID orderId) {
    return requests.stream().map(request -> toEntity(request, orderId)).toList();
  }
}
