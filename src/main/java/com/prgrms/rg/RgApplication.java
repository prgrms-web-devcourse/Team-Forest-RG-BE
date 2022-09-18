package com.prgrms.rg;

import com.prgrms.rg.infrastructure.message.RemoteMessageSenderDelegator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RgApplication {

  private static final Logger log = LoggerFactory.getLogger(RgApplication.class.getName());

  public static void main(String[] args) {
    System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    try {
      SpringApplication.run(RgApplication.class, args);
    } catch (Exception exception) {
      RemoteMessageSenderDelegator.send(exception);
      throw exception;
    }
    RemoteMessageSenderDelegator.send("SERVER APPLICATION INITIALIZED WITHOUT EXCEPTION");

  }

}
