package com.lh.ecommerce.subcriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lh.ecommerce.dto.response.Session;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

@RequiredArgsConstructor
@Slf4j
public class MessageSubscriber2 implements MessageListener {

  private final ObjectMapper objectMapper;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    try {
      Session data = objectMapper.readValue(message.getBody(), Session.class);
      log.info("Message received: " + data);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
