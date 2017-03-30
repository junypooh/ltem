package com.kt.giga.home.b2b.util;

import com.kt.giga.home.b2b.domain.AuthorityInfo;
import com.kt.giga.home.b2b.domain.B2bManager;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

/**
 * <pre>
 * com.kt.giga.home.b2b.util
 *      SessionUtils
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author junypooh
 * @see
 * @since 2017-01-05 오후 5:37
 */
public class SessionUtils {

    private static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }

        return context.getAuthentication();
    }


    public static B2bManager getUser() {
        Authentication authentication = getAuthentication();
        if(authentication != null){
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof B2bManager)) {
                return null;
            }
            return (B2bManager) principal;
        }
        return null;
    }


    public static List<AuthorityInfo> getAuthorities() {
        B2bManager b2BManager = getUser();
        if (b2BManager == null) {
            return Collections.emptyList();
        }
        return b2BManager.getAuthorities();
    }


    public static boolean hasRole(String role) {
        for (GrantedAuthority authorityInfo : getAuthorities()) {
            if (StringUtils.equals(role, authorityInfo.getAuthority())) {
                return true;
            }
        }
        return false;
    }


    public static boolean hasAnyRole(String... roles) {
        for (GrantedAuthority authorityInfo : getAuthorities()) {
            if (ArrayUtils.contains(roles, authorityInfo.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
