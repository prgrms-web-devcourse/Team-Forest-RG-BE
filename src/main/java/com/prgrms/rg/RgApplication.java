package com.prgrms.rg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class RgApplication {
	public static void main(String[] args) {
		// 애플리케이션을 ec2에서만 사용할 것이 아니므로, spring cloud aws에서 ec2메타데이터를 받아오는 것이 불필요함
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
		SpringApplication.run(RgApplication.class, args);

	}
}
