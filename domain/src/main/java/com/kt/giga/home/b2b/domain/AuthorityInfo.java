package com.kt.giga.home.b2b.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * <pre>
 * com.kt.giga.home.b2b.domain
 *      AuthorityInfo
 *
 * 사용자 권한 정보
 *
 * </pre>
 *
 * @author junypooh
 * @see
 * @since 2016-12-21 오전 10:40
 */
@Data
public class AuthorityInfo implements GrantedAuthority {

    public AuthorityInfo(String authority) {
        this.authority = "ROLE_" + authority;
    }

    private String authority;
}
