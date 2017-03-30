package com.kt.giga.home.b2b.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by junypooh on 2017-01-24.
 */
public class LoginAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

//        logger.debug("{}", exception);
        if (exception instanceof SessionAuthenticationException) {

            exception = new SessionAuthenticationException("현재 해당 아이디로 접속 중입니다. 중복 로그인은 불가능 합니다.");
        }

        super.onAuthenticationFailure(request, response, exception);
    }
}
