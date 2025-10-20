package com.lh.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EcommerceBeApplication {

  public static void main(String[] args) {
    SpringApplication.run(EcommerceBeApplication.class, args);
  }
}
