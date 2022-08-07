package com.prgrms.rg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;

import com.prgrms.rg.RgApplication;

/**
 * <p>JPA 관련 설정들을 작동시키는 Bean입니다.</p>
 */
@EnableJpaRepositories(bootstrapMode = BootstrapMode.DEFERRED, basePackageClasses = RgApplication.class)
@EnableJpaAuditing
@Configuration
public class JpaConfiguration {

}
