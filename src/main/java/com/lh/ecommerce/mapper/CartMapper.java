package com.lh.ecommerce.mapper;

import com.lh.ecommerce.adapter.UploadFileAdapter;
import com.lh.ecommerce.dto.response.CartResponse;
import com.lh.ecommerce.entity.CartEntity;
import java.util.List;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {ImageKeyUrlMapping.class})
public interface CartMapper {
  List<CartResponse> toCartsResponse(
      List<CartEntity> cartEntities, @Context UploadFileAdapter uploadFileAdapter);

  @Mapping(target = "price", source = "product.price")
  @Mapping(target = "productId", source = "product.id")
  @Mapping(target = "productName", source = "product.name")
  @Mapping(target = "productMainImage", source = "image.key", qualifiedByName = "keyToUrl")
  CartResponse toCartResponse(CartEntity cartEntity, @Context UploadFileAdapter uploadFileAdapter);
}
