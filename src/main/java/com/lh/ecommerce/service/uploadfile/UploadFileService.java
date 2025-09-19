package com.lh.ecommerce.service.uploadfile;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploadFileService {

  public List<String> uploadMultipleFiles(List<MultipartFile> files) {
    return null;
  }
}
