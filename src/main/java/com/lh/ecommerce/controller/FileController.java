package com.lh.ecommerce.controller;

import com.lh.ecommerce.service.uploadfile.UploadFileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
  private final UploadFileService uploadFileService;

  @PostMapping
  public List<String> upload(@RequestParam("files") List<MultipartFile> files) {
    return uploadFileService.uploadMultipleFiles(files);
  }
}
