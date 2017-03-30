package com.kt.giga.home.b2b.gateway;


import com.kt.giga.home.b2b.gateway.configurations.ServletContainerConfiguration;
import com.kt.giga.home.b2b.gateway.filters.RedirectingFilter;
import com.kt.giga.home.b2b.gateway.ribbon.ManagerApi;
import com.kt.giga.home.b2b.gateway.ribbon.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication(
        scanBasePackages = {"com.kt.giga.home.b2b.gateway.filters", "com.kt.giga.home.b2b.gateway.configurations"}
)
@Slf4j
@Configuration
@EnableConfigurationProperties(ServletContainerConfiguration.class)
@RibbonClients({
        @RibbonClient(name = "ManagerAPI", configuration = ManagerApi.class),
        @RibbonClient(name = "SMS-Service", configuration = SmsService.class)
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RedirectingFilter redirectingFilter() {
        return new RedirectingFilter();
    }

}
