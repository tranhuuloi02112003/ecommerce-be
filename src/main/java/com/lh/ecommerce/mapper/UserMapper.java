package com.lh.ecommerce.mapper;

import com.lh.ecommerce.dto.response.UserResponse;
import com.lh.ecommerce.entity.UserEntity;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserResponse toUsersResponse(UserEntity entity);

  List<UserResponse> toUsersResponseList(List<UserEntity> entities);
}
