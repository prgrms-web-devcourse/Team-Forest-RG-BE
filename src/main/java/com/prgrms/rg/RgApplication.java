package com.prgrms.rg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.prgrms.rg.web.common.message.CriticalMessageSender;

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
		CriticalMessageSender.send("API 서버가 성공적으로 실행되었습니다!");

	}
}
