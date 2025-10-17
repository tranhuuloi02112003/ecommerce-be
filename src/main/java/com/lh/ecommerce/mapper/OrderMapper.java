package com.lh.ecommerce.mapper;

import com.lh.ecommerce.dto.response.OrderResponse;
import com.lh.ecommerce.dto.response.PageBaseResponse;
import com.lh.ecommerce.entity.OrderEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface OrderMapper {

  @Mapping(target = "customerName", source = "address.name")
  @Mapping(target = "customerPhone", source = "address.phone")
  OrderResponse toResponse(OrderEntity orderEntity);

  List<OrderResponse> toResponse(List<OrderEntity> orderEntities);

  default PageBaseResponse<OrderResponse> toPageResponse(Page<OrderEntity> pageData) {
    List<OrderResponse> orderResponses =
        pageData.getContent().stream().map(this::toResponse).toList();
    return new PageBaseResponse<>(orderResponses, pageData);
  }
}
