package com.lh.ecommerce.mapper;

import com.lh.ecommerce.dto.response.AddressResponse;
import com.lh.ecommerce.dto.resquest.AddressRequest;
import com.lh.ecommerce.entity.AddressEntity;
import java.util.List;
import java.util.UUID;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AddressMapper {
  @Mapping(target = "userId", expression = "java(userId)")
  @Mapping(target = "defaultAddress", ignore = true)
  AddressEntity toEntity(AddressRequest request, UUID userId);

  void updateEntity(AddressRequest request, @MappingTarget AddressEntity entity);

  AddressResponse toResponse(AddressEntity entity);

  List<AddressResponse> toResponse(List<AddressEntity> entities);
}
