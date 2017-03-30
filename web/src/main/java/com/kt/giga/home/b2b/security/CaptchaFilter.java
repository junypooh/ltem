package com.kt.giga.home.b2b.security;

import com.google.code.kaptcha.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * com.kt.giga.home.b2b.security
 * <p>
 * Created by cecil on 2017. 2. 6..
 */
@Slf4j
public class CaptchaFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req     = (HttpServletRequest) request;
        HttpSession        session = req.getSession();
        if (session != null) {
            request.setAttribute(Constants.KAPTCHA_SESSION_KEY, session.getAttribute(Constants.KAPTCHA_SESSION_KEY));
        }

        chain.doFilter(request, response);
    }
}
