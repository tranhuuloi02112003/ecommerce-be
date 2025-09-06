package com.lh.ecommerce.mapper;

import com.lh.ecommerce.dto.response.User;
import com.lh.ecommerce.entity.UserEntity;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toDto(UserEntity entity);

  List<User> toDtoList(List<UserEntity> entities);
}
