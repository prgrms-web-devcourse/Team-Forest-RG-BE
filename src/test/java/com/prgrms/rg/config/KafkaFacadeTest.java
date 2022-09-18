package com.prgrms.rg.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class KafkaFacadeTest {

  KafkaFacade kafkaFacade = KafkaFacade.getInstance();

  @Test
  @DisplayName("testName")
  void test() {

    int transmissionCount = 0;
    final int EXPECTED_COUNT = 40;

    for (int i = 0; i < EXPECTED_COUNT; i++) {
      kafkaFacade.send("hello");
    }
  }

}
