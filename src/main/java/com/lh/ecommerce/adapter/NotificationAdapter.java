package com.lh.ecommerce.adapter;

import com.lh.ecommerce.dto.identity.CertificationParam;
import com.lh.ecommerce.dto.identity.CertificationTokenResponse;
import com.lh.ecommerce.dto.resquest.EmailVerificationRequest;
import com.lh.ecommerce.repository.EmailClient;
import com.lh.ecommerce.repository.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationAdapter {
  static final String BEARER_PREFIX = "Bearer ";

  final EmailClient emailClient;
  final IdentityClient identityClient;

  @Value("${client.keycloak.client-id}")
  String clientId;

  @Value("${client.keycloak.client-secret}")
  String clientSecret;

  public void sendVerificationEmail(String email, String verificationCode, String fullName) {

    String accessToken = getAccessToken();

    EmailVerificationRequest request =
        EmailVerificationRequest.builder()
            .to(email)
            .token(verificationCode)
            .fullName(fullName)
            .build();

    emailClient.sendVerificationEmail(BEARER_PREFIX + accessToken, request);
  }

  private String getAccessToken() {
    CertificationParam param =
        CertificationParam.builder()
            .grant_type("client_credentials")
            .client_id(clientId)
            .client_secret(clientSecret)
            .scope("openid")
            .build();

    CertificationTokenResponse response = identityClient.certificationToken(param);
    return response.getAccessToken();
  }
}
