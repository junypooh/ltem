package com.kt.giga.home.b2b.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by junypooh on 2017-02-09.
 * <pre>
 * com.kt.giga.home.b2b.security.CustomAccessDeniedHandler
 *
 * AccessDeniedHandler
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-02-09 오후 1:22
 */
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private String accessDeniedUrl;

    public CustomAccessDeniedHandler(String accessDeniedUrl) {
        this.accessDeniedUrl = accessDeniedUrl;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.debug("Exception : " + accessDeniedException);
        // Ajax를 통해 들어온것인지 파악한다
        String ajaxHeader = request.getHeader("X-Ajax-call");

        //InvalidCsrfTokenException
        //MissingCsrfTokenException
        //CsrfException
        //org.springframework.security.web.csrf.MissingCsrfTokenException: Could not verify the provided CSRF token because your session was not found.
        //AccessDeniedException: 접근이 거부되었습니다.
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding("UTF-8");

        if (ajaxHeader == null) {

            // null로 받은 경우는 X-Ajax-call 헤더 변수가 없다는 의미이기 때문에 ajax가 아닌 일반적인 방법으로 접근했음을 의미한다
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth != null) {
                Object principal = auth.getPrincipal();
                if (principal instanceof UserDetails) {
                    String username = ((UserDetails) principal).getUsername();
                    request.setAttribute("username", username);
                }
            }
            request.setAttribute("errormsg", accessDeniedException);
            request.setAttribute("message", accessDeniedException.getMessage());
            request.getRequestDispatcher(accessDeniedUrl).forward(request, response);

        } else {

            String result;

            if ("true".equals(ajaxHeader)) { // true로 값을 받았다는 것은 ajax로 접근했음을
                // 의미한다
                result = "{\"result\" : \"fail\", \"errormsg\" : \"" + accessDeniedException.getMessage() + "\"}";
            } else { // 헤더 변수는 있으나 값이 틀린 경우이므로 헤더값이 틀렸다는 의미로 돌려준다
                result = "{\"result\" : \"fail\", \"errormsg\" : \"Access Denied(Header Value Mismatch)\"}";
            }
            response.getWriter().print(result);
            response.getWriter().flush();
        }
    }
}
