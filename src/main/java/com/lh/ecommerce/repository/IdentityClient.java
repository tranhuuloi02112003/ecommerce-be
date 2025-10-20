package com.lh.ecommerce.repository;

import com.lh.ecommerce.dto.identity.CertificationParam;
import com.lh.ecommerce.dto.identity.CertificationTokenResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "identity-client", url = "${client.keycloak.base-url}")
public interface IdentityClient {

  @PostMapping(
      value = "/realms/demo/protocol/openid-connect/token",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  CertificationTokenResponse certificationToken(@QueryMap CertificationParam certificationParam);
}
