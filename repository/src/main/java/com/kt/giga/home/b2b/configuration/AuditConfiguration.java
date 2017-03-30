package com.kt.giga.home.b2b.configuration;

import com.kt.giga.home.b2b.services.CurrentDateTimeService;
import org.springframework.context.annotation.Bean;

/**
 * Created by cecil on 2017. 1. 13..
 */
public class AuditConfiguration {

    @Bean
    CurrentDateTimeService currentDateTimeService() {

        return new CurrentDateTimeService();
    }
}
