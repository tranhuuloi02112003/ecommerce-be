package com.lh.ecommerce.controller;

import com.lh.ecommerce.adapter.UploadFileAdapter;
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
public class UploadFileController {
  private final UploadFileAdapter uploadFileAdapter;

  @PostMapping
  public List<String> uploadFile(@RequestParam("files") List<MultipartFile> files) {
    return uploadFileAdapter.uploadMultipleFiles(files);
  }
}
