package com.prgrms.rg.infrastructure.message.exception;

import com.prgrms.rg.infrastructure.message.utils.FormattedMessageFactory;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LocalLoggingAdvice {

  @Pointcut("this(com.prgrms.rg.infrastructure.message.exception.HttpErrorMessageSender)")
  public void messageSenderPointCut() { /* pointcut namespace */ }

  @Before(value = "messageSenderPointCut() && args(exception, request)", argNames = "exception,request")
  public void createExceptionLog(Exception exception, HttpServletRequest request) {
    log.warn(FormattedMessageFactory.createHttpMetadataMessageFrom(request), exception);
  }

  @Before(value = "messageSenderPointCut() && args(message,..)", argNames = "message")
  public void createLog(String message) {
    log.warn(message);
  }
}
