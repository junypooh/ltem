package com.kt.giga.home.b2b.configurations.feign;

import feign.Logger;
import feign.Request;
import org.springframework.context.annotation.Bean;

/**
 * com.kt.giga.home.b2b.configurations
 * <p>
 * Created by cecil on 2017. 3. 1..
 */
public class XroshotFeignConfiguration {

    @Bean
    public Logger.Level logger() {
        return Logger.Level.FULL;
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(3000, 7000);
    }
}
