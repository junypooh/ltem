package com.kt.giga.home.b2b.web.controller;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.MyPageInfo;
import com.kt.giga.home.b2b.web.services.ICommonService;
import com.kt.giga.home.b2b.web.services.IManagerService;
import com.kt.giga.home.b2b.web.services.IMyPageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by DaDa on 2017-01-25.
 */
@Slf4j
@Controller
@RequestMapping("/mypage")
public class MyPageController {

    @Autowired
    IMyPageService iMyPageService;

    @Autowired
    ICommonService iCommonService;

    @Autowired
    IManagerService iManagerService;

    @Autowired
    StandardPasswordEncoder standardPasswordEncoder;

    @GetMapping(value = "")
    public String myPage(Model model, @AuthenticationPrincipal B2bManager currentManager) {
        model.addAttribute("myPageInfo", iMyPageService.selectMyPageInfo(currentManager));
        return "mypage/modify";
    }

    @PostMapping(value = "/save")
    public String save(
            Model model,
            @Valid MyPageInfo myPageInfo,
            BindingResult bindingResult, @AuthenticationPrincipal B2bManager currentManager) {

        model.addAttribute("myPageInfo", myPageInfo);

        // 비밀번호 변경일 경우
        if(!(StringUtils.trimToNull(myPageInfo.getPasswd()) == null &&
                StringUtils.trimToNull(myPageInfo.getNewPasswd()) == null &&
                StringUtils.trimToNull(myPageInfo.getNewPasswdConfirm()) == null)) {
            if(!(StringUtils.trimToNull(myPageInfo.getPasswd()) != null &&
                    StringUtils.trimToNull(myPageInfo.getNewPasswd()) != null &&
                    StringUtils.trimToNull(myPageInfo.getNewPasswdConfirm()) != null)) {

                if(StringUtils.trimToNull(myPageInfo.getPasswd()) == null) {
                    bindingResult.addError(new FieldError("myPageInfo", "passwd", "비밀번호를 입력해주세요."));
                }

                if(StringUtils.trimToNull(myPageInfo.getNewPasswd()) == null) {
                    bindingResult.addError(new FieldError("myPageInfo", "newPasswd", "새로운 비밀번호를 입력해주세요."));
                }

                if(StringUtils.trimToNull(myPageInfo.getNewPasswdConfirm()) == null) {
                    bindingResult.addError(new FieldError("myPageInfo", "newPasswdConfirm", "새로운 비밀번호 확인을 입력해주세요."));
                }
            } else {
                // 현재 비밀번호가 맞지 않을 경우
                if (!iCommonService.doesPasswordMatch(myPageInfo.getPasswd(), currentManager.getPassword())) {
                    bindingResult.addError(new FieldError("myPageInfo", "passwd", "비밀번호를 다시 확인해주세요."));
                }

            }

        }

        if (bindingResult.hasErrors()) {
            return "/mypage/modify";
        }

        iMyPageService.updateMyPageInfo(currentManager, myPageInfo);

        return "redirect:/";
    }

    /**
     * 휴대폰 인증번호 발송
     * @param myPageInfo
     * @return
     */
    @PostMapping(value = "/sendAuthentication")
    @ResponseBody
    public Object sendAuthentication(@ModelAttribute MyPageInfo myPageInfo,
                                     @AuthenticationPrincipal B2bManager currentManager) {
        return iMyPageService.sendAuthNumber(myPageInfo, currentManager);
    }

    /**
     * 인증번호 확인
     * @param myPageInfo
     * @param bindingResult
     * @param currentManager
     * @return
     */
    @PostMapping(value = "/doesAuthNumberMatch")
    @ResponseBody
    public Object doesAuthNumberMatch(MyPageInfo myPageInfo,
                                      BindingResult bindingResult,
                                      @AuthenticationPrincipal B2bManager currentManager,
                                      Model model) {

        model.addAttribute("myPageInfo", myPageInfo);

        if(StringUtils.isEmpty(myPageInfo.getAuthenticationNumber())) {
            bindingResult.addError(new FieldError("myPageInfo","authenticationNumber", "인증번호를 입력해주세요."));
        }

        if (bindingResult.hasErrors()) {
            return "/mypage/modify";
        }

        return iMyPageService.doesAuthNumberMatch(myPageInfo, currentManager);

    }



}
