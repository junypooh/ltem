package com.kt.giga.home.b2b.web.configuration;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * com.kt.giga.home.b2b.web.configuration
 * <p>
 * Created by cecil on 2017. 2. 2..
 */
public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {

    public SecurityInitializer() {
        super(SecurityConfiguration.class, HazelcastHttpSessionConfig.class);
    }
}
