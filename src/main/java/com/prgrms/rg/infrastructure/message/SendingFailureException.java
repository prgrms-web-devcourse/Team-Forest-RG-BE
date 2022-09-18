package com.prgrms.rg.infrastructure.message;

import java.io.IOException;

public class SendingFailureException extends RuntimeException {

  public SendingFailureException(IOException exception) {
    super(exception);
  }
}
