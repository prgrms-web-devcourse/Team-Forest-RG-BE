package com.prgrms.rg;

import com.prgrms.rg.web.common.message.CriticalMessageSender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RgApplication {

  public static void main(String[] args) throws Exception {
    System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    try {
      SpringApplication.run(RgApplication.class, args);
    } catch (Exception exception) {
      CriticalMessageSender.send(exception);
      throw exception;
    }

    CriticalMessageSender.send("SERVER APPLICATION INITIALIZED WITHOUT EXCEPTION");

  }
}
