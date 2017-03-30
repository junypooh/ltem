package com.kt.giga.home.b2b.web.controller;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.TermInfo;
import com.kt.giga.home.b2b.web.services.IManagerTermService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by DaDa on 2017-03-16.
 */
@Controller
@Slf4j
@RequestMapping("provision")
public class ProvisionController {

    @Autowired
    private IManagerTermService iManagerTermService;

    /**
     * 약관 동의 View
     * @param currentManager
     * @param model
     * @return
     */
    @GetMapping(value = "/termAgreement")
    public String termAgreement(@AuthenticationPrincipal B2bManager currentManager, Model model) {

        if(StringUtils.isEmpty(currentManager.getUsername())) {
            return "login/login";
        }

        String url = "redirect:/";
        Map<String, Object> map = iManagerTermService.selectManagerTermsCheck(currentManager);
        if ((int) map.get("code") < 0) {
            List<TermInfo> terms = (List<TermInfo>) map.get("terms");
            model.addAttribute("terms", terms);
            url = "provision/termAgreement";
        }

        model.addAttribute("crcn", "\r\n");

        return url;
    }

    /**
     * 약관 동의 취소
     * @param request
     * @param response
     * @return
     */
    @GetMapping(value = "/termAgreement/cancel")
    public String termAgreementCeacel(HttpServletRequest request, HttpServletResponse response) {
        // 강제 로그 아웃 처리.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }


    /**
     * 약관 동의 저장
     * @param currentManager
     * @param termSeqs
     * @return
     */
    @GetMapping(value = "/termAgreement/save")
    public String termAgreementsave(HttpServletRequest request, @AuthenticationPrincipal B2bManager currentManager, Integer[] termSeqs) {
        iManagerTermService.managerTermsSave(currentManager, termSeqs);

        request.getSession().removeAttribute("termsAgreeRequired");

        return "redirect:/";
    }


}
