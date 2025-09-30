package com.lh.ecommerce.mapper;

import com.lh.ecommerce.dto.response.UserResponse;
import com.lh.ecommerce.dto.resquest.RegisterRequest;
import com.lh.ecommerce.entity.UserEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(target = "password", ignore = true)
  UserEntity toUserEntity(RegisterRequest request);

  UserResponse toResponse(UserEntity entity);

  List<UserResponse> toResponse(List<UserEntity> entities);
}
