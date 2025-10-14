package com.lh.ecommerce.adapter;

import com.lh.ecommerce.dto.response.UploadFileResponse;
import com.lh.ecommerce.utils.FileUtils;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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

  public String getUrlS3(String valueKey) {
    GetObjectRequest getObjectRequest =
        GetObjectRequest.builder().bucket(bucketName).key(valueKey).build();

    GetObjectPresignRequest getObjectPresignRequest =
        GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(1))
            .getObjectRequest(getObjectRequest)
            .build();

    return s3Presigner.presignGetObject(getObjectPresignRequest).url().toString();
  }

  public List<String> getUrlsFromKeys(List<String> keys) {
    List<CompletableFuture<String>> futures =
        keys.stream()
            .map(k -> CompletableFuture.supplyAsync(() -> getUrlS3(k)).exceptionally(ex -> null))
            .toList();

    return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
        .thenApply(v -> futures.stream().map(CompletableFuture::join).toList())
        .join();
  }

  @SneakyThrows
  public UploadFileResponse uploadFile(MultipartFile file) {
    String key = FileUtils.createNewName(file.getOriginalFilename());

    PutObjectRequest putObjectRequest =
            PutObjectRequest.builder().bucket(bucketName).key(key).build();

    s3Client.putObject(
            putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

    return UploadFileResponse.builder().url(getUrlS3(key)).key(key).build();
  }

  public List<UploadFileResponse> uploadMultipleFiles(List<MultipartFile> files) {
    List<CompletableFuture<UploadFileResponse>> futures =
        files.stream()
            .map(
                f ->
                    CompletableFuture.supplyAsync(() -> uploadFile(f))
                        .exceptionally(ex -> null))
            .toList();

    return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
        .thenApply(v -> futures.stream().map(CompletableFuture::join).toList())
        .join();
  }
}
