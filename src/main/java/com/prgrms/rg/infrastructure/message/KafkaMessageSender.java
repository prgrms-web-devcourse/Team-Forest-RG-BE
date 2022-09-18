package com.prgrms.rg.infrastructure.message;

import com.prgrms.rg.config.KafkaFacade;

public class KafkaMessageSender implements MessageSender {

  private final KafkaFacade kafka = KafkaFacade.getInstance();

  @Override
  public boolean isServiceEnabled() {
    return kafka.isServiceWorking();
  }

  @Override
  public void send(Exception exception) {

  }

  @Override
  public void send(String message) {
    kafka.send(message);

  }
}
