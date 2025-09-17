package com.lh.ecommerce.config;

import com.lh.ecommerce.utils.SecurityUtils;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

  @Bean(name = "auditorProvider")
  public AuditorAware<UUID> auditorProvider() {
    return () -> Optional.ofNullable(SecurityUtils.getCurrentUserId());
  }
}
