package com.kt.giga.home.b2b.security;

import com.kt.giga.home.b2b.domain.B2bManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;

import java.util.List;

/**
 * <pre>
 * com.kt.giga.home.b2b.security
 *      LogoutListener
 *
 * Auto Logout Listener
 *
 * </pre>
 *
 * @author junypooh
 * @see
 * @since 2017-01-06 오후 5:52
 */
@Slf4j
public class LogoutListener implements ApplicationListener<SessionDestroyedEvent> {

    @Override
    public void onApplicationEvent(SessionDestroyedEvent applicationEvent) {

        List<SecurityContext> lstSecurityContext = applicationEvent.getSecurityContexts();

        for (SecurityContext securityContext : lstSecurityContext) {
            B2bManager b2BManager = (B2bManager) securityContext.getAuthentication().getPrincipal();

            log.debug(" LOGOUT SUCCESS [" + b2BManager.getUsername()+"]");

            // TODO 로그아웃 이력 쌓기
        }
    }
}
