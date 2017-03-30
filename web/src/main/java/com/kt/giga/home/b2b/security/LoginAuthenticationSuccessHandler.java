package com.kt.giga.home.b2b.security;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.web.services.IManagerService;
import com.kt.giga.home.b2b.web.services.IManagerTermService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <pre>
 * com.kt.giga.home.b2b.security
 *      LoginAuthenticationSuccessHandler
 *
 * Login handler
 *
 * </pre>
 *
 * @author junypooh
 * @see
 * @since 2017-01-09 오후 3:37
 */
@Slf4j
public class LoginAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private IManagerService iManagerService;

    @Autowired
    private IManagerTermService iManagerTermService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {

        B2bManager b2BManager = (B2bManager)authentication.getPrincipal();

        log.debug(" LOGIN SUCCESS [" + b2BManager.getUsername()+"]" );

        // TODO 로그인 이력 쌓기

        // 최종 로그인 시간 UPDATE
        iManagerService.updateLastLoggedIn(b2BManager);

        // 30일 경과 비밀번호 변경 페이지 전환
        if (iManagerService.isPasswordChangeRequired(b2BManager)) {
            clearAuthenticationAttributes(request);

            String targetUrl = request.getContextPath() + "/find/pwdChange";
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
        // 약관동의 페이지 전환
        else if(iManagerTermService.isManagerTermsCheck(b2BManager)) {
            clearAuthenticationAttributes(request);

            request.getSession().setAttribute("termsAgreeRequired", true);

            String targetUrl = request.getContextPath() + "/provision/termAgreement";
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }

    }
}
