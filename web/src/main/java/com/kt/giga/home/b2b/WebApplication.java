package com.kt.giga.home.b2b;

import com.kt.giga.home.b2b.services.IInitialDataProvider;
import com.kt.giga.home.b2b.web.configuration.HazelcastHttpSessionConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
//import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * <pre>
 * com.kt.giga.home.b2b.web
 *      Application
 *
 * Application Starter Class
 *
 * </pre>
 *
 * @author junypooh
 * @see
 * @since 2016-12-12 오전 11:18
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "com.kt.giga.home.b2b")
@EnableAspectJAutoProxy
@EnableDiscoveryClient
@EnableFeignClients
//@EnableHystrix
@EnableConfigurationProperties(
        value = {HazelcastHttpSessionConfig.class}
)
public class WebApplication implements CommandLineRunner {

    @Autowired
    private IInitialDataProvider dataProvider;

    public static void main(String[] args) {

        SpringApplication.run(WebApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing data...");
        dataProvider.initData();
        log.info("Data initialization finished.");
    }
}
