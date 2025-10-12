package com.lh.ecommerce.mapper;

import com.lh.ecommerce.adapter.UploadFileAdapter;
import com.lh.ecommerce.service.image.ImageError;
import java.util.List;
import org.mapstruct.Context;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
public class ImageKeyUrlMapping {
  @Named("keyToUrl")
  public String keyToUrl(String key, @Context UploadFileAdapter adapter) {
    if (!StringUtils.hasText(key)) {
      return null;
    }
    return adapter.getUrlS3(key);
  }

  @Named("keysToUrls")
  public List<String> keysToUrls(List<String> keys, @Context UploadFileAdapter adapter) {
    validateKeys(keys);
    return adapter.getUrlsFromKeys(keys);
  }

  private void validateKeys(List<String> keys) {
    if (CollectionUtils.isEmpty(keys)) {
      throw ImageError.keyNotEmpty().get();
    }

    boolean hasValidKey = keys.stream().allMatch(StringUtils::hasText);
    if (!hasValidKey) {
      throw ImageError.keyNotEmpty().get();
    }
  }
}