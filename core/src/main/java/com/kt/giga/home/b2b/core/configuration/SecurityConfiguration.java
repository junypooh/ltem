package com.kt.giga.home.b2b.core.configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * <pre>
 * com.kt.giga.home.b2b.web.configuration
 *      SecurityConfiguration
 *
 * Spring Security Configuration
 *
 * </pre>
 *
 * @author junypooh
 * @see
 * @since 2017-01-03 오후 5:23
 */
//@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/**").permitAll();

    }


}
