package com.prgrms.rg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;

@EnableJpaRepositories(bootstrapMode = BootstrapMode.DEFERRED)
@EnableJpaAuditing
@Configuration
public class JpaConfiguration {

}
