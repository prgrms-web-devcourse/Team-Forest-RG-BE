package com.prgrms.rg.infrastructure.message.exception;

import com.prgrms.rg.infrastructure.message.RemoteMessageSenderDelegator;
import com.prgrms.rg.infrastructure.message.utils.FormattedMessageFactory;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
@Slf4j
public class OuterSystemHttpErrorMessageSender implements HttpErrorMessageSender {

  private void sendMessage(HttpServletRequest request, String message) {
    try {
      String prefixMessage = FormattedMessageFactory.createHttpMetadataMessageFrom(request);
      RemoteMessageSenderDelegator.send(prefixMessage + "\n" + message);
    } catch (Exception systemException) {
      log.error(systemException.getMessage(), systemException);
    }
  }

  @Override
  public void send(Exception exception, HttpServletRequest request) {
    sendMessage(request, FormattedMessageFactory.createStackTraceMessageFrom(exception));
  }

  @Override
  public void send(String message, HttpServletRequest request) {
    sendMessage(request, message);
  }
}
