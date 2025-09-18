package com.lh.ecommerce.service.color;

import com.lh.ecommerce.dto.response.ColorResponse;
import com.lh.ecommerce.dto.resquest.ColorRequest;
import com.lh.ecommerce.entity.ColorEntity;
import com.lh.ecommerce.mapper.ColorMapper;
import com.lh.ecommerce.repository.ColorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ColorService {
  private final ColorRepository colorRepository;
  private final ColorMapper colorMapper;

  public ColorResponse create(ColorRequest request) {
    ColorEntity entity = colorMapper.toEntity(request);

    ColorEntity saved = colorRepository.save(entity);
    return colorMapper.toResponse(saved);
  }

  public List<ColorResponse> findAll() {
    List<ColorEntity> entities = colorRepository.findAll();
    return colorMapper.toResponse(entities);
  }
}
