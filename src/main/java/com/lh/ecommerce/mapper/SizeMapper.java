package com.lh.ecommerce.mapper;

import com.lh.ecommerce.dto.response.SizeResponse;
import com.lh.ecommerce.dto.resquest.SizeRequest;
import com.lh.ecommerce.entity.SizeEntity;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SizeMapper {
  SizeResponse toResponse(SizeEntity entity);

  List<SizeResponse> toResponse(List<SizeEntity> entities);

  SizeEntity toEntity(SizeRequest request);
}
