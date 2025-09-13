package com.lh.ecommerce.service.uploadfile;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class UploadFileService {

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

  @Value("${s3.bucket-name}")
  private String bucketName;

  @SneakyThrows
  public String uploadFile(MultipartFile fileUpload) {
    PutObjectRequest putObjectRequest =
        PutObjectRequest.builder().bucket(bucketName).key(fileUpload.getOriginalFilename()).build();

    s3Client.putObject(
        putObjectRequest,
        RequestBody.fromInputStream(fileUpload.getInputStream(), fileUpload.getSize()));

    return getUrlS3(fileUpload.getOriginalFilename());
  }

  public String getUrlS3(String fileName) {
    GetObjectRequest getObjectRequest =
        GetObjectRequest.builder().bucket(bucketName).key(fileName).build();

    GetObjectPresignRequest getObjectPresignRequest =
        GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10))
            .getObjectRequest(getObjectRequest)
            .build();
    return s3Presigner.presignGetObject(getObjectPresignRequest).url().toString();
  }

  public List<String> uploadMultipleFiles(List<MultipartFile> files) {
    List<String> uploadedUrls = new ArrayList<>();

    files.forEach(multipartFile -> uploadedUrls.add(uploadFile(multipartFile)));
    return uploadedUrls;
  }
}
