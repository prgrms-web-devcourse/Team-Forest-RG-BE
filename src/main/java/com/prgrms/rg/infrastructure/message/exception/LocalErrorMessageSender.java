package com.prgrms.rg.infrastructure.message.exception;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 로컬 환경에서 예외 메시지를 로그에 출력합니다.
 */
@Slf4j
@Component
@Profile("!prod")
public class LocalErrorMessageSender implements HttpErrorMessageSender {

  @Override
  public void send(Exception exception, HttpServletRequest request) {/*aop로 구현*/}

  @Override
  public void send(String message, HttpServletRequest request) {/*aop로 구현*/}
}
