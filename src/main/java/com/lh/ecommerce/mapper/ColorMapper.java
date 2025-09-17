package com.lh.ecommerce.mapper;

import com.lh.ecommerce.dto.response.ColorResponse;
import com.lh.ecommerce.dto.resquest.ColorRequest;
import com.lh.ecommerce.entity.ColorEntity;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ColorMapper {
  ColorResponse toResponse(ColorEntity entity);

  List<ColorResponse> toResponse(List<ColorEntity> entities);

  ColorEntity toEntity(ColorRequest request);
}
