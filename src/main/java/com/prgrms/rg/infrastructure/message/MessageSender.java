package com.prgrms.rg.infrastructure.message;

public interface MessageSender {

  boolean isServiceEnabled();

  void send(Exception exception);

  void send(String message);
}
