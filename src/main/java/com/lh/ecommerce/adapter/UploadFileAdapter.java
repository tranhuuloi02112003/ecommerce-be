package com.lh.ecommerce.adapter;

import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Component
@RequiredArgsConstructor
public class UploadFileAdapter {

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

  @Value("${s3.bucket-name}")
  private String bucketName;

  @SneakyThrows
  private String uploadFile(MultipartFile fileUpload) {
    PutObjectRequest putObjectRequest =
        PutObjectRequest.builder().bucket(bucketName).key(fileUpload.getOriginalFilename()).build();

    s3Client.putObject(
        putObjectRequest,
        RequestBody.fromInputStream(fileUpload.getInputStream(), fileUpload.getSize()));

    return getUrlS3(fileUpload.getOriginalFilename());
  }

  private String getUrlS3(String fileName) {
    GetObjectRequest getObjectRequest =
        GetObjectRequest.builder().bucket(bucketName).key(fileName).build();

    GetObjectPresignRequest getObjectPresignRequest =
        GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofDays(7))
            .getObjectRequest(getObjectRequest)
            .build();
    return s3Presigner.presignGetObject(getObjectPresignRequest).url().toString();
  }

  public List<String> uploadMultipleFiles(List<MultipartFile> files) {
    return files.stream().map(this::uploadFile).toList();
  }
}
