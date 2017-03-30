package com.kt.giga.home.b2b.web.configuration;

import com.kt.giga.home.b2b.common.util.ApplicationContextUtils;
import com.kt.giga.home.b2b.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.query.spi.EvaluationContextExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.hazelcast.HazelcastSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

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
@Profile("!local")
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private ManagementServerProperties managementServerProperties;

    @Autowired
    private HazelcastSessionRepository sessionRepository;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

/*
    @Autowired
    private HybridAuthenticationEntryPoint hybridAuthenticationEntryPoint;
*/

    @Bean
    public StandardPasswordEncoder standardPasswordEncoderBean() {
        return new StandardPasswordEncoder();
    }

    @Bean
    public ApplicationContextUtils applicationContextUtilsBean() throws Exception {
        return new ApplicationContextUtils();
    }

    @Bean
    public LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler() {
        return new LoginAuthenticationSuccessHandler();
    }

    @Bean
    public LoginAuthenticationFailureHandler loginAuthenticationFailureHandler() {
        LoginAuthenticationFailureHandler loginAuthenticationFailureHandler = new LoginAuthenticationFailureHandler();
        loginAuthenticationFailureHandler.setDefaultFailureUrl("/login/login?error");

        return loginAuthenticationFailureHandler;
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler("/error/403");
    }

    @Bean
    public LogoutListener logoutListenerBean() throws Exception {
        return new LogoutListener();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry((FindByIndexNameSessionRepository) sessionRepository);
    }

    @Bean
    EvaluationContextExtension securityExtension() {
        return new SecurityEvaluationContextExtension();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http

                .authorizeRequests()
                .antMatchers("/login", "/login/autoLogout", "/login/captchaGenerateKey").permitAll()
                .antMatchers("/find/find*/**", "/service/**", "/agree/**").permitAll()
                .antMatchers("/find/sendAuthentication", "/find/doesAuthNumberMatch").permitAll()
                .antMatchers(managementServerProperties.getContextPath() + "/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf()
                .and()
                .addFilterBefore(new CaptchaFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(loginAuthenticationSuccessHandler())
                .failureHandler(loginAuthenticationFailureHandler())
                .and()
                .logout()
                .logoutUrl("/logout")
//                .logoutSuccessHandler(loggingLogoutSuccessHandler()) // 로그아웃 listener 와 동시 처리되어 주석처리
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "hazelcast.sessionId")
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler())
//                .authenticationEntryPoint(hybridAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true) // 중복 로그인 허용 안함
                .sessionRegistry(sessionRegistry());

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**"); // Static resources are ignored
    }
}
