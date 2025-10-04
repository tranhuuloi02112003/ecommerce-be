package com.lh.ecommerce.mapper;

import com.lh.ecommerce.dto.response.UserResponse;
import com.lh.ecommerce.dto.resquest.RegisterRequest;
import com.lh.ecommerce.dto.resquest.UpdateInfoRequest;
import com.lh.ecommerce.entity.UserEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(target = "password", ignore = true)
  UserEntity toUserEntity(RegisterRequest request);

  void updateEntity(UpdateInfoRequest request, @MappingTarget UserEntity entity);

  UserResponse toResponse(UserEntity entity);

  List<UserResponse> toResponse(List<UserEntity> entities);
}
