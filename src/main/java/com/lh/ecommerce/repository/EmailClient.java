package com.lh.ecommerce.repository;

import com.lh.ecommerce.dto.resquest.EmailVerificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "email-client", url = "${email.service.base-url}")
public interface EmailClient {

  @PostMapping("/api/emails")
  void sendVerificationEmail(
      @RequestHeader("authorization") String token, @RequestBody EmailVerificationRequest request);
}
