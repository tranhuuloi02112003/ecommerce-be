package com.lh.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new StringRedisSerializer()); // Keys as strings
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // Values as JSON
    template.setHashKeySerializer(new StringRedisSerializer()); // Hash keys as strings
    template.setHashValueSerializer(
        new GenericJackson2JsonRedisSerializer()); // Hash values as JSON
    template.afterPropertiesSet();
    return template;
  }
}
