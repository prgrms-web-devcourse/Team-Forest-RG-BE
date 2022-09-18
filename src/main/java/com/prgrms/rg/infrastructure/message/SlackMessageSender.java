package com.prgrms.rg.infrastructure.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.rg.infrastructure.message.utils.FormattedMessageFactory;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.web.client.RestTemplate;

/**
 * 스프링에서 처리하지 못한 예외 메시지를 자바 애플리케이션 종료 직전에 외부 시스템에 전송합니다.
 * TODO : 추상화
 */
@Slf4j
public class SlackMessageSender implements MessageSender {

  private final String SLACK_CHANNEL_ID = System.getenv("SLACK_CHANNEL_NAME");
  private final String SLACK_AUTH_TOKEN = System.getenv("SLACK_AUTH_TOKEN");
  private final String SYSTEM_PROFILE = System.getProperty("spring.profiles.active");

  private void sendHttpRequest(String body) throws IOException {
    var restTemplate = new RestTemplate();
    restTemplate.postForLocation("https://slack.com/api/char.postMesssage", body,
        Map.of("Content-Type", ContentType.APPLICATION_JSON, "Authorization", "Bearer " + SLACK_AUTH_TOKEN)
    );
  }

  private String createHttpBodyFrom(String message) throws JsonProcessingException {
    ObjectMapper json = new ObjectMapper();
    String emoji = ":meow_sad:\t";
    StringBuilder messageWithEmoji = new StringBuilder();
    messageWithEmoji.append(emoji.repeat(20));
    messageWithEmoji.append("\n").append(message).append("\n");
    messageWithEmoji.append(emoji.repeat(20));
    return json.writeValueAsString(Map.of("channel", SLACK_CHANNEL_ID, "text", messageWithEmoji));

  }

  @Override
  public void send(Exception exception) {
    String stackTraceMessage = FormattedMessageFactory.createStackTraceMessageFrom(exception);
    send(stackTraceMessage);
  }

  @Override
  public void send(String message) {
    try {
      var messageBody = createHttpBodyFrom(message);
      sendHttpRequest(messageBody);
    } catch (IOException exception) {
      throw new SendingFailureException(exception);
    }
  }

  @Override
  public boolean isServiceEnabled() {
    if (SYSTEM_PROFILE == null || !SYSTEM_PROFILE.contains("prod")) {
      return false;
    }
    try {
      send("BOOTSTRAP");
    } catch (RuntimeException exception) {
      log.warn(exception.getMessage(), exception);
      return false;
    }

    return true;
  }

}
