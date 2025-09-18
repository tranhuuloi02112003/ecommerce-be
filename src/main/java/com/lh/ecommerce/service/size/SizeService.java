package com.lh.ecommerce.service.size;

import com.lh.ecommerce.dto.response.SizeResponse;
import com.lh.ecommerce.dto.resquest.SizeRequest;
import com.lh.ecommerce.entity.SizeEntity;
import com.lh.ecommerce.mapper.SizeMapper;
import com.lh.ecommerce.repository.SizeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SizeService {
  private final SizeRepository sizeRepository;
  private final SizeMapper sizeMapper;

  public SizeResponse create(SizeRequest request) {
    SizeEntity entity = sizeMapper.toEntity(request);

    SizeEntity saved = sizeRepository.save(entity);
    return sizeMapper.toResponse(saved);
  }

  public List<SizeResponse> findAll() {
    List<SizeEntity> entities = sizeRepository.findAll();
    return sizeMapper.toResponse(entities);
  }
}
