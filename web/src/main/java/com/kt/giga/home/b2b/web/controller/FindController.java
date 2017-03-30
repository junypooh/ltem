package com.kt.giga.home.b2b.web.controller;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.FindAccountInfo;
import com.kt.giga.home.b2b.service.IMaskingService;
import com.kt.giga.home.b2b.web.services.IManagerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by junypooh on 2017-03-08.
 * <pre>
 * com.kt.giga.home.b2b.web.controller.FindController
 *
 * 아이디/비밀번호 찾기 Controller
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-08 오전 10:52
 */
@Controller
@Slf4j
@RequestMapping("find")
public class FindController {

    @Autowired
    private IManagerService iManagerService;

    @Autowired
    private IMaskingService iMaskingService;

    @Autowired
    private StandardPasswordEncoder standardPasswordEncoder;

    /**
     * 아이디 찾기 페이지
     * @param findAccountInfo
     */
    @GetMapping(value = "findId")
    public void findId(@ModelAttribute FindAccountInfo findAccountInfo) {
    }

    /**
     * 아이디 찾기/비밀번호 찾기 : 휴대폰 인증번호 발송
     * @param findAccountInfo
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "sendAuthentication")
    @ResponseBody
    public Object sendAuthentication(@Valid @ModelAttribute FindAccountInfo findAccountInfo,
                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            Map<String, Object> rtnMap = new HashMap<>();
            rtnMap.put("errorCd", -1);
            rtnMap.put("msg", "필수값이 존재하지 않습니다.");
            return rtnMap;
        }

        return iManagerService.sendAuthNumber(findAccountInfo);
    }

    /**
     * 아이디 찾기/비밀번호 찾기 : 인증번호 확인
     * @param findAccountInfo
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "doesAuthNumberMatch")
    @ResponseBody
    public Object doesAuthNumberMatch(@Valid @ModelAttribute FindAccountInfo findAccountInfo,
                                      BindingResult bindingResult) {

        if(StringUtils.isEmpty(findAccountInfo.getAuthenticationNumber())) {
            bindingResult.addError(new FieldError("findAccountInfo","authenticationNumber", "인증번호를 입력해주세요."));
        }

        if (bindingResult.hasErrors()) {

            if(StringUtils.isNotBlank(findAccountInfo.getId()))
                return "find/findAuth";
            else
                return "find/findId";
        }

        FindAccountInfo accountInfo = iManagerService.doesAuthNumberMatch(findAccountInfo);

        Map<String, Object> rtnMap = new HashMap<>();
        if(accountInfo == null) {
            rtnMap.put("errorCd", -1);
            rtnMap.put("msg", "인증번호가 일치하지 않습니다.");
        } else {
            rtnMap.put("errorCd", 0);
            rtnMap.put("msg", "인증이 완료 되었습니다.");
            rtnMap.put("mgrSeq", accountInfo.getMgrSeq());
        }
        return rtnMap;

    }

    /**
     * 아이디 찾기 처리
     * @param findAccountInfo
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "findId")
    public String executeFindId(@Valid @ModelAttribute FindAccountInfo findAccountInfo,
                                BindingResult bindingResult, Model model) {

        if(StringUtils.isEmpty(findAccountInfo.getAuthenticationNumber())) {
            bindingResult.addError(new FieldError("findAccountInfo","authenticationNumber", "인증번호를 입력해주세요."));
        }

        if (bindingResult.hasErrors()) {
            return "find/findId";
        }

        FindAccountInfo accountInfo = iManagerService.doesAuthNumberMatch(findAccountInfo);

        if(accountInfo == null) {
            findAccountInfo.setAuthenticationNumber("");
            model.addAttribute("error", true);
            return "find/findId";
        } else {

            iManagerService.initializeAuthNumber(findAccountInfo.getMgrSeq());

            model.addAttribute("userId", iMaskingService.maskFromEnd(accountInfo.getId(), 3));
            return "find/findIdSuccess";
        }

    }

    /**
     * 비밀번호 찾기 페이지
     * @param findAccountInfo
     */
    @GetMapping(value = "findPwd")
    public void findPwd(@ModelAttribute FindAccountInfo findAccountInfo) {
    }

    /**
     * 비밀번호 찾기 페이지
     * @param findAccountInfo
     */
    @PostMapping(value = "findPwd")
    public String findPwdAuth(@ModelAttribute FindAccountInfo findAccountInfo, BindingResult bindingResult) {

        if(StringUtils.isEmpty(findAccountInfo.getId())) {
            bindingResult.addError(new FieldError("findAccountInfo","username", "아이디를 입력해주세요."));
        }

        if(!iManagerService.existsUserId(findAccountInfo.getId())) {
            bindingResult.addError(new FieldError("findAccountInfo","username", "입력한 아이디는 찾을 수 없습니다."));
        }

        if (bindingResult.hasErrors()) {
            return "find/findPwd";
        }

        return "find/findPwdAuth";
    }

    /**
     * 비밀번호 찾기 인증 확인
     * @param findAccountInfo
     * @param bindingResult
     * @param model
     * @return
     */
    @PostMapping(value = "findPwdCheck")
    public String findPwdCheck(@Valid FindAccountInfo findAccountInfo, BindingResult bindingResult, Model model) {

        if(StringUtils.isEmpty(findAccountInfo.getAuthenticationNumber())) {
            bindingResult.addError(new FieldError("findAccountInfo","authenticationNumber", "인증번호를 입력해주세요."));
        }

        if (bindingResult.hasErrors()) {
            return "find/findPwdAuth";
        }

        FindAccountInfo accountInfo = iManagerService.doesAuthNumberMatch(findAccountInfo);

        if(accountInfo == null) {
            findAccountInfo.setAuthenticationNumber("");
            model.addAttribute("findAccountInfo", findAccountInfo);
            model.addAttribute("error", true);
            return "find/findPwdAuth";
        } else {

            model.addAttribute("findAccountInfo", accountInfo);
            iManagerService.initializeAuthNumber(findAccountInfo.getMgrSeq());

            return "find/findPwdChange";
        }

    }

    /**
     * 비밀번호 찾기 > 비밀번호 변경
     * @param findAccountInfo
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "findPwdChange")
    public String findPwdChange(@Valid @ModelAttribute FindAccountInfo findAccountInfo, BindingResult bindingResult, @AuthenticationPrincipal B2bManager b2bManager) {

        if(StringUtils.isEmpty(findAccountInfo.getNewPasswd())) {
            bindingResult.addError(new FieldError("findAccountInfo","newPasswd", "비밀번호를 입력해주세요."));
        }

        if(StringUtils.isEmpty(findAccountInfo.getNewPasswdConfirm())) {
            bindingResult.addError(new FieldError("findAccountInfo","newPasswdConfirm", "비밀번호를 입력해주세요."));
        }

        if (bindingResult.hasErrors()) {
            return "find/findPwdChange";
        }

        // 비밀번호 변경 처리.
        iManagerService.updatePassword(findAccountInfo);

        return "redirect:/";
    }

    /**
     * 비밀번호 초기화 > 비밀번호 변경 화면 이동
     * @param currentManager
     * @param model
     * @return
     */
    @GetMapping(value = "pwdChange")
    public String pwdChange(@AuthenticationPrincipal B2bManager currentManager, Model model) {

        if(StringUtils.isEmpty(currentManager.getUsername())) {
            return "login/login";
        }

        FindAccountInfo findAccountInfo = new FindAccountInfo();
        findAccountInfo.setMgrSeq(currentManager.getMgrSeq());
        findAccountInfo.setName(currentManager.getName());
        findAccountInfo.setTelephone(currentManager.getTelephone());

        model.addAttribute("findAccountInfo", findAccountInfo);

        return "find/pwdChange";
    }

    /**
     * 비밀번호 초기화 > 비밀번호 변경
     * @param findAccountInfo
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "pwdChangeProcess")
    public String pwdChangeProcess(@Valid @ModelAttribute FindAccountInfo findAccountInfo
            , BindingResult bindingResult, Model model
            , @AuthenticationPrincipal B2bManager currentManager
            , HttpServletRequest request, HttpServletResponse response) {

        if(StringUtils.isEmpty(findAccountInfo.getNewPasswd())) {
            bindingResult.addError(new FieldError("findAccountInfo","newPasswd", "비밀번호를 입력해주세요."));
        }

        if(StringUtils.isEmpty(findAccountInfo.getNewPasswdConfirm())) {
            bindingResult.addError(new FieldError("findAccountInfo","newPasswdConfirm", "비밀번호를 입력해주세요."));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("findAccountInfo", findAccountInfo);
            return "find/pwdChange";
        }

        // 비밀번호 변경 처리.
        iManagerService.updatePassword(findAccountInfo);

        // 강제 로그 아웃 처리.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/";
    }
}
