package com.lh.ecommerce.adapter;

import com.lh.ecommerce.dto.resquest.EmailVerificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationAdapter {

  private final RestTemplate restTemplate;

  @Value("${email.service.url:http://localhost:8081/api/emails}")
  private String emailServiceUrl;

  public void sendVerificationEmail(String email, String verificationCode, String fullName) {
    try {
      EmailVerificationRequest request =
          EmailVerificationRequest.builder()
              .to(email)
              .token(verificationCode)
              .fullName(fullName)
              .build();

      restTemplate.postForEntity(emailServiceUrl, request, Void.class);
      log.info("Verification email sent to: {}", email);
    } catch (Exception e) {
      log.error("Failed to send verification email to: {}", email, e);
    }
  }
}
