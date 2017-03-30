package com.kt.giga.home.b2b.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <pre>
 * com.kt.giga.home.b2b.domain
 *      B2bManager
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author junypooh
 * @see
 * @since 2017-01-12 오후 5:33
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class B2bManager implements UserDetails {

    /** 이름 */
    private String name;

    /** 관리자 sequence */
    private Long mgrSeq;

    /** 이메일 */
    private String email;

    /** 전화번호 */
    private String telephone;

    /** 인증번호 */
    private String authenticationNumber;

    /** 로그인 용 아이디 */
    private String username;

    /** 로그인 용 비밀번호 */
    private String password;

    /** spring security 사용 권한s */
    private List<AuthorityInfo> authorities;

    private Role role;

    public short getAuthorityCd() {
        return this.role.getAuthorityCd();
    }

    public String getAuthorityName()
    {
        String authorityName = this.role.name();
        authorityName = authorityName.replaceFirst(authorityName.substring(1), authorityName.substring(1).toLowerCase());
        authorityName = authorityName.replaceAll("_", "-");

        return authorityName;
    }

    /** 회사 코드 */
    private String companyCd;

    /** 회사 명 */
    private String companyName;

    /** 직급 */
    private String department;

    /** 관리 계약건 */
    private String contractsName;

    /** 관리자 상태 코드 */
    private Short mgrStatusCd;

    /** 관리자 상태 명 */
    public String getMgrStatusName() {
        Short mgrStatusCd = this.getMgrStatusCd();
        String mgrStatusName = null;
        if(mgrStatusCd == 30)
            mgrStatusName = "사용";
        if(mgrStatusCd == 40)
            mgrStatusName = "정지";
        return mgrStatusName;
    }

    /** 변경일 */
    public LocalDateTime modified;

    /** 변경일 String 변환 */
    public String getModifiedToString() {
        String modifiedToString = "";
        if( this.modified != null) {
            modifiedToString = this.modified.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return modifiedToString;
    }

    /** 마지막 로그인 일시 */
    public LocalDateTime lastLoggedIn;

    /** 비밀 번호 초기화 여부 */
    private boolean isPasswordInitialized;

    /**
     * UserDetails default variable
     */
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

}
