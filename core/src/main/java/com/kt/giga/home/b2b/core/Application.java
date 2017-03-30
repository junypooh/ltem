package com.kt.giga.home.b2b.core;

import com.kt.giga.home.b2b.configuration.PersistenceContext;
import org.apache.cxf.spring.boot.autoconfigure.CxfAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Import;

/**
 * Created by 민우 on 2016-11-24.
 */

@SpringBootApplication(scanBasePackages = "com.kt.giga.home.b2b", exclude = CxfAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.kt.giga.home.b2b")
@Import(PersistenceContext.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
