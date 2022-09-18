package com.prgrms.rg.infrastructure.message;

import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RemoteMessageSenderDelegator {

  private static final List<MessageSender> CANDIDATES =
      List.of(
          new SlackMessageSender(),
          new KafkaMessageSender()
      );
  private static MessageSender messageSender;

  static {
    for (MessageSender candidate : CANDIDATES) {
      if (candidate.isServiceEnabled()) {
        messageSender = candidate;
        break;
      }
    }
    messageSender = messageSender == null ? new FakeMessageSender() : messageSender;
  }

  public static void send(Exception exception) {
    messageSender.send(exception);
  }

  public static void send(String message) {
    messageSender.send(message);
  }

  private static class FakeMessageSender implements MessageSender {

    @Override
    public boolean isServiceEnabled() {
      return true;
    }

    @Override
    public void send(Exception exception) {/*no-op*/}

    @Override
    public void send(String message) {/*no-op*/}
  }
}
