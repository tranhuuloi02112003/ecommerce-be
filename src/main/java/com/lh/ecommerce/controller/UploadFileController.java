package com.lh.ecommerce.controller;

import com.lh.ecommerce.adapter.UploadFileAdapter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

  @GetMapping
  public String getFile(@RequestParam("file-name") String fileName) {
    return uploadFileAdapter.getUrlS3(fileName);
  }
}
