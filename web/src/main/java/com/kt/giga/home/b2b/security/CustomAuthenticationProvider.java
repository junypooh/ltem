package com.kt.giga.home.b2b.security;

import com.google.code.kaptcha.Constants;
import com.kt.giga.home.b2b.domain.AuthorityInfo;
import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.web.services.IManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <pre>
 * com.kt.giga.home.b2b.security.CustomAuthenticationProvider
 *
 * 로그인 처리 Provider
 *
 * </pre>
 *
 * @author junypooh
 * @see
 * @since 2017-01-05 오후 12:23
 */
@Slf4j
@Component("customAuthenticationProvider")
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    private final StandardPasswordEncoder standardPasswordEncoder;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    private final IManagerService iManagerService;

    @Autowired
    public CustomAuthenticationProvider(
            @Qualifier(value = "userDetailsService") UserDetailsService userDetailsService
            , StandardPasswordEncoder standardPasswordEncoder
            , IManagerService iManagerService) {

        this.userDetailsService = userDetailsService;
        this.standardPasswordEncoder = standardPasswordEncoder;
        this.iManagerService = iManagerService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        String captchaCode     = (String) request.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        String userCaptchaCode = request.getParameter("captchaCode");

        log.debug("captchaCode : " + captchaCode);
        log.debug("userCaptchaCode : " + userCaptchaCode);

        log.debug("username : " + username);
        log.debug("password : " + password);

        B2bManager          b2BManager;
        List<AuthorityInfo> authorities;

        try {

            // 사용자 정보 조회(by Id)
            b2BManager = (B2bManager) userDetailsService.loadUserByUsername(username);


            if (captchaCode == null || !captchaCode.equals(userCaptchaCode)) {

                // 보안문자 실패 정보 update
                iManagerService.updateCaptchaFailCount(b2BManager);

                throw new BadCredentialsException("보안문자가 일치하지 않습니다.");
            }

            if (!standardPasswordEncoder.matches(password, b2BManager.getPassword())) {

                // 비밀번호 틀림 실패 정보 update
                iManagerService.updatePasswordFailCount(b2BManager);
                throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
            }

            authorities = b2BManager.getAuthorities();

        } catch (UsernameNotFoundException e) {
            log.info(e.toString());
            throw new UsernameNotFoundException(e.getMessage());
        } catch (BadCredentialsException e) {
            log.info(e.toString());
            throw new BadCredentialsException(e.getMessage());
        } catch (LockedException e) {
            log.info(e.toString());
            throw new LockedException(e.getMessage());
        } catch (Exception e) {
            log.error("Error : ", e);
            throw new RuntimeException(e.getMessage());
        }

        // 캡챠코드, 비밀번호 실패 횟수 초기화
        iManagerService.initializeFailCount(b2BManager);

        return new UsernamePasswordAuthenticationToken(b2BManager, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
