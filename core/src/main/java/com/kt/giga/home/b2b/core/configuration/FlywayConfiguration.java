package com.kt.giga.home.b2b.core.configuration;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by cecil on 2017. 1. 12..
 */
@Profile("local")
@Configuration
public class FlywayConfiguration {

    @Bean
    public FlywayMigrationStrategy FlywayCleanStrategy() {

        return flyway -> {
            flyway.clean();
            flyway.migrate();
        };
    }

}
