package com.prgrms.rg.config;

import com.prgrms.rg.RgApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories(bootstrapMode = BootstrapMode.DEFERRED, basePackageClasses = RgApplication.class)
@EnableJpaAuditing
@EnableScheduling
@Configuration
public class JpaConfiguration {

}
