package com.kt.giga.home.b2b.configuration;

import com.kt.giga.home.b2b.audit.AuditingDateTimeProvider;
import com.kt.giga.home.b2b.audit.UsernameAuditorAware;
import com.kt.giga.home.b2b.base.SpringContextBridge;
import com.kt.giga.home.b2b.crypto.ICryptoService;
import com.kt.giga.home.b2b.services.IDateTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by cecil on 2017. 1. 13..
 */
@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
@EnableJpaRepositories(basePackages = {"com.kt.giga.home.b2b.repository"})
@EntityScan(
        basePackageClasses = {Jsr310JpaConverters.class},
        basePackages = {"com.kt.giga.home.b2b.entity"}
)
@EnableTransactionManagement
@Import(AuditConfiguration.class)
public class PersistenceContext {


    @Autowired
    ICryptoService cryptoService;

    @Bean
    SpringContextBridge springContextBridge() {

        return new SpringContextBridge(cryptoService);
    }

    @Bean
    AuditorAware<String> auditorProvider(@Value("${b2b.auditor.fixedName:#{null}}") String fixedUsername) {
        return new UsernameAuditorAware(fixedUsername);
    }

    @Bean
    DateTimeProvider dateTimeProvider(IDateTimeService dateTimeService) {
        return new AuditingDateTimeProvider(dateTimeService);
    }
}
