package com.kt.giga.home.b2b.audit;

import com.kt.giga.home.b2b.domain.B2bManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * createdBy, modifiedBy를 자동 입력하기 위한 Class
 *
 * @author Sung Bum, Lee
 */
@Slf4j
public class UsernameAuditorAware implements AuditorAware<String> {

    private String fixedUsername;

    /**
     * 항상 같은 createdBy, modifiedBy를 입력하기 위해 사용한다.
     *
     * @param fixedUsername createdBy, modifiedBy에 입력될 <code>String</code>
     */
    public UsernameAuditorAware(String fixedUsername) {
        this.fixedUsername = fixedUsername;
    }

    @Override
    public String getCurrentAuditor() {

        String userName;
        if (fixedUsername != null) {
            userName = fixedUsername;
        } else {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            }
            try {
                B2bManager principal = (B2bManager) authentication.getPrincipal();
                userName = principal.getUsername();
            } catch (ClassCastException e) {
                log.warn("Can't find authentication information.");
                userName = null;
            }
        }

        log.debug("Current Auditor : {}", userName);
        return userName;
    }
}
