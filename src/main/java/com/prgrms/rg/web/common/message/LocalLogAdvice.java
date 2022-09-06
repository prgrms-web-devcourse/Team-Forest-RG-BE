package com.prgrms.rg.web.common.message;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LocalLogAdvice {

  @Pointcut("this(com.prgrms.rg.web.common.message.ExceptionMessageSender)")
  public void messageSenderPointCut(){ /* pointcut namespace */ }

  @Before(value ="messageSenderPointCut() && args(exception, request)", argNames = "exception,request")
  public void createExceptionLog(Exception exception, HttpServletRequest request){
    log.warn(MessageFactory.createHttpMessage(request), exception);
  }

  @Before(value ="messageSenderPointCut() && args(message,..)", argNames = "message")
  public void createLog(String message){
    log.warn(message);
  }
}
