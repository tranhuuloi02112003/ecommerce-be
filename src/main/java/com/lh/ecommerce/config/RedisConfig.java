package com.lh.ecommerce.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lh.ecommerce.subcriber.MessageSubscriber;
import com.lh.ecommerce.subcriber.MessageSubscriber2;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
  private final ObjectMapper objectMapper;

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);

    StringRedisSerializer stringSerializer = new StringRedisSerializer();
    GenericJackson2JsonRedisSerializer jsonSerializer =
        new GenericJackson2JsonRedisSerializer(objectMapper);

    template.setKeySerializer(stringSerializer);
    template.setValueSerializer(jsonSerializer);

    template.setHashKeySerializer(stringSerializer);
    template.setHashValueSerializer(jsonSerializer);

    template.afterPropertiesSet();
    return template;
  }

  @Bean
  ChannelTopic topic() {
    return new ChannelTopic("pubsub:queue");
  }

  @Bean
  ChannelTopic topic2() {
    return new ChannelTopic("pubsub:queue");
  }

  @Bean
  public RedisMessageListenerContainer redisContainer(
      RedisConnectionFactory connectionFactory,
      MessageListenerAdapter messageListener,
      ChannelTopic topic) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addMessageListener(messageListener, topic);
    return container;
  }

  @Bean
  public RedisMessageListenerContainer redisContainer2(
      RedisConnectionFactory connectionFactory,
      MessageListenerAdapter messageListener2,
      ChannelTopic topic2) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addMessageListener(messageListener2, topic2);
    return container;
  }

  @Bean
  public MessageListenerAdapter messageListener(MessageSubscriber subscriber) {
    return new MessageListenerAdapter(subscriber);
  }

  @Bean
  public MessageListenerAdapter messageListener2(MessageSubscriber2 subscriber) {
    return new MessageListenerAdapter(subscriber);
  }
}
