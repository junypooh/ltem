package com.kt.giga.home.b2b.web.controller;

import com.google.code.kaptcha.servlet.KaptchaExtend;
import com.kt.giga.home.b2b.entity.BizContract;
import com.kt.giga.home.b2b.web.services.IBannerService;
import com.kt.giga.home.b2b.web.services.ICommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

/**
 * <pre>
 * com.kt.giga.home.b2b.web.controller
 *      LoginController
 *
 * 로그인 Controller
 *
 * </pre>
 *
 * @author junypooh
 * @see
 * @since 2017-01-04 오후 3:52
 */
@Controller
@Slf4j
@RequestMapping("login")
public class LoginController extends KaptchaExtend {

    @Autowired
    private IBannerService iBannerService;

    @Autowired
    protected ICommonService iCommonService;

    /**
     * captcha Key Generate
     * model MUST contain HTML with <img src="${pageContext.request.contextPath}/login/captchaGenerateKey" /> tag
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @GetMapping(value = "captchaGenerateKey")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.captcha(request, response);
    }

    /**
     * 로그인 페이지
     *
     * @param model
     * @param error
     * @param logout
     * @return
     */
    @GetMapping
    public String login(HttpServletRequest request, Model model, String error, String logout) {

        if(error != null) {
            model.addAttribute("error", true);

            HttpSession session = request.getSession();
            AuthenticationException exception = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

            if(exception != null) {

                if(exception instanceof SessionAuthenticationException) {
                    model.addAttribute("duplicate", true);
                }

                if(exception instanceof LockedException) {
                    model.addAttribute("blocking", true);
                }

                if(exception.getMessage().contains("보안문자")) {
                    model.addAttribute("secretCode", true);
                }
            }
        }
        if(logout != null) {
            model.addAttribute("logout", true);
        }

        model.addAttribute("banners", iBannerService.selectCurrentTrueAndCategory((short) 1));
        return "login/login";
    }

    /**
     * 자동 로그아웃 페이지
     * @return
     */
    @GetMapping(value = "autoLogout")
    public String autoLogout(HttpServletRequest request, HttpServletResponse response) {

        // 강제 로그 아웃 처리.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "login/autoLogout";
    }


    private void logPreviousBizContract(BizContract bizContract) {

        if (bizContract == null) {
            return;
        }

        log.debug("{} : {}", bizContract.getClass().getName(), bizContract.getSerialNumber());

        Set<BizContract> previousBizContracts = bizContract.getPreviousBizContracts();
        if (previousBizContracts != null) {

            previousBizContracts.forEach(this::logPreviousBizContract);
        }


    }

    /**
     * 배너 다운로드
     * @param fileSeq
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/bannerDown")
    public void downloadFile(@RequestParam Long fileSeq, HttpServletRequest request, HttpServletResponse response, String type)
            throws Exception {

        iCommonService.downloadFile(fileSeq, false,  request, response, "BANNER");
    }

}
