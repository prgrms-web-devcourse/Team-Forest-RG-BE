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

		/*
		 * 초기 profile entry point를 확인합니다. (prod 아니면 default)
		 */
		var initialProfile = System.getProperty("spring.profiles.active");
		try {
			SpringApplication.run(RgApplication.class, args);
		} catch (Exception exception) {
			// 초기 profile이 prod를 포함할 경우에만 특정 모듈에 메시지를 보냅니다.
			if (initialProfile != null && initialProfile.matches("prod")) {
				CriticalMessageSender.send(exception);
			}
			throw exception;
		}

	}
}
