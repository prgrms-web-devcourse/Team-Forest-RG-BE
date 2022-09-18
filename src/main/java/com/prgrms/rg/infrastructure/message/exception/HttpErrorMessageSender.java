package com.prgrms.rg.infrastructure.message.exception;

import javax.servlet.http.HttpServletRequest;

public interface HttpErrorMessageSender {

  void send(Exception exception, HttpServletRequest request);

  void send(String message, HttpServletRequest request);
}
