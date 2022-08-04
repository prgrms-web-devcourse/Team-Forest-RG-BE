package com.prgrms.rg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.prgrms.rg.infrastructure.cloud.CriticalMessageSender;

@EnableJpaAuditing
@SpringBootApplication
public class RgApplication {
	public static void main(String[] args) throws Exception {
		// 애플리케이션을 ec2에서만 사용할 것이 아니므로, spring cloud aws에서 ec2메타데이터를 받아오는 것이 불필요함
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
		try {
			SpringApplication.run(RgApplication.class, args);
		} catch (Exception exception) {
			// 웹 애플리케이션에서 처리되지 못한 예외에 관한 내용들을 다른 시스템으로 전송합니다.
			CriticalMessageSender.send(exception);
			throw exception;
		}
		// 웹 애플리케이션이 성공적으로 초기화 됐을 때 성공 메시지를 다른 시스템으로 전송합니다.
		CriticalMessageSender.send("SERVER APPLICATION INITIALIZED WITHOUT EXCEPTION");

	}
}
