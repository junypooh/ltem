package com.kt.giga.home.b2b.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

/**
 * com.kt.giga.home.b2b.core.configuration
 * <p>
 * Created by cecil on 2017. 1. 21..
 */
@Configuration
public class ValidatorConfiguration {

    @Bean
    Validator localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

}
