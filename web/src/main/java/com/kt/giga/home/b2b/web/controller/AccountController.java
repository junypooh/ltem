package com.kt.giga.home.b2b.web.controller;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.kt.giga.home.b2b.cache.CacheAccountEdit;
import com.kt.giga.home.b2b.common.constant.CodeConstants;
import com.kt.giga.home.b2b.domain.AccountInfo;
import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.ContractInfo;
import com.kt.giga.home.b2b.entity.Manager;
import com.kt.giga.home.b2b.util.PageUtil;
import com.kt.giga.home.b2b.web.services.CommonService;
import com.kt.giga.home.b2b.web.services.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by DaDa on 2017-02-23.
 */
@Controller
@RequestMapping("/accounts")
@Slf4j
public class AccountController {

    @Autowired
    private IAccountService iAccountService;

    @Autowired
    private PageUtil pageUtil;

    @Autowired
    private CommonService commonService;

    @Autowired
    private HazelcastInstance hazelcastInstance;

    /**
     * 계정관리 목록
     * @param model
     * @param pageable
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('SUPERVISOR')")
    @GetMapping(value = "")
    public String list(Model model, @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC, size = 30) Pageable pageable, @AuthenticationPrincipal B2bManager currentManager) {
        Map<String, Object> map      = iAccountService.selectAccountManagers(currentManager, pageable, null, null, null);
        List<B2bManager> managerList = (List<B2bManager>) map.get("list");
        Page<Manager>    page        = (Page<Manager>) map.get("page");

        model.addAttribute("managerList", managerList);
        model.addAttribute("pageHtml", pageUtil.makePageHtml(page));
        model.addAttribute("pageNumber", page.getNumber());
        model.addAttribute("pageTotalCnt", page.getTotalElements());

        return "accounts/list";
    }

    /**
     * 계정관리 목록 검색 Ajax
     * @param pageable
     * @param status
     * @param column
     * @param searchWord
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('SUPERVISOR')")
    @GetMapping(value = "listSearch")
    @ResponseBody
    public Object listSearch(@PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC, size = 30) Pageable pageable
            , String status, String column, String searchWord, @AuthenticationPrincipal B2bManager currentManager) {
        Map<String, Object> map      = iAccountService.selectAccountManagers(currentManager, pageable, status, column, searchWord);
        List<B2bManager> managerList = (List<B2bManager>) map.get("list");
        Page<Manager>    page        = (Page<Manager>) map.get("page");

        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("managerList", managerList);
        rtnMap.put("pageHtml", pageUtil.makePageHtml(page));
        rtnMap.put("pageNumber", page.getNumber());
        rtnMap.put("pageTotalCnt", page.getTotalElements());

        return rtnMap;
    }

    /**
     * 계정 삭제 가능한지 여부 확인
     * @param mgrSeqs
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('SUPERVISOR')")
    @PostMapping(value = "deleteConfirm")
    @ResponseBody
    public Object deleteAccountConfirm(Long[] mgrSeqs) {
        return iAccountService.deleteAccountConfirm(mgrSeqs);
    }

    /**
     * 계정 삭제
     * @param mgrSeqs
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('SUPERVISOR')")
    @PostMapping(value = "delete")
    @ResponseBody
    public Object deleteAccount(Long[] mgrSeqs) {
        return iAccountService.deleteAccountManagers(mgrSeqs);
    }


    /**
     * 가려진 정보 해제
     * @param password
     * @param mgrSeq
     * @param currentManager
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('SUPERVISOR')")
    @PostMapping(value = "unmask")
    @ResponseBody
    public Object unmaskAccount(String password, Long mgrSeq, @AuthenticationPrincipal B2bManager currentManager) {
        Map<String, Object> rtnMap = new HashMap<>();

        if(commonService.doesPasswordMatch(password, currentManager.getPassword())) {
            rtnMap.put("manager", iAccountService.getUnmaskedAccountManager(mgrSeq));
            rtnMap.put("errorCd", 0);
        } else {
            rtnMap.put("errorCd", -1);
            rtnMap.put("msg", "비밀번호가 일치하지 않습니다.");
        }

        return rtnMap;
    }

    /**
     * 계정관리 > 계정관리 상세
     * @param mgrSeq
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('SUPERVISOR')")
    @GetMapping(value = "/{mgrSeq}")
    public String accountDetail(@PathVariable("mgrSeq") Long mgrSeq, Model model) {
        Map<String, Object> map = iAccountService.selectAccountManager(mgrSeq);
        model.addAttribute("manager", map.get("manager"));
        model.addAttribute("contract", map.get("contract"));
        model.addAttribute("unmask", false);
        return "accounts/detail";
    }

    /**
     * 계정관리 > 계정관리 상세 > 계정관리 수정페이지 이동
     * @param mgrSeq
     * @param password
     * @param model
     * @param currentManager
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('SUPERVISOR')")
    @PostMapping(value = "/{mgrSeq}")
    public String getUnmaskedAccountDetail(@PathVariable("mgrSeq") Long mgrSeq, String password, String companyCd, short authorityCd, Model model, @AuthenticationPrincipal B2bManager currentManager) {

        if (StringUtils.isNotBlank(password) && commonService.doesPasswordMatch(password, currentManager.getPassword())) {


            if(authorityCd == currentManager.getRole().getAuthorityCd()) {

                Map<String, Object> accountMap = iAccountService.selectUnmaskAccountManager(mgrSeq);
                model.addAttribute("manager", accountMap.get("manager"));
                model.addAttribute("contract", accountMap.get("contract"));
                model.addAttribute("unmask", true);
                return "accounts/detail";
            } else {

                Map<String, Object> accountMap = iAccountService.getUnmaskedAccountManagerDetail(mgrSeq, companyCd, currentManager);
                model.addAttribute("accountInfo", accountMap.get("accountInfo"));
                model.addAttribute("contract", accountMap.get("contract"));
                model.addAttribute("bizContract", accountMap.get("bizContract"));
                model.addAttribute("authorityCd", accountMap.get("authorityCd"));

                // 가려진 정보 해제 여부 cache 등록 (5분 expired)
                IMap<Object, Object> map = hazelcastInstance.getMap(CodeConstants.ACCOUNT_CACHE_MAP_NAME);
                map.put(currentManager.getMgrSeq(), new CacheAccountEdit(currentManager.getMgrSeq(), mgrSeq), 5, TimeUnit.MINUTES);
                return "accounts/modify";
            }
        } else {
            model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("doesNotPasswordMatched", true);

            Map<String, Object> accountMap = iAccountService.selectAccountManager(mgrSeq);
            model.addAttribute("manager", accountMap.get("manager"));
            model.addAttribute("contract", accountMap.get("contract"));
            return "accounts/detail";
        }
    }

    /**
     * 계정관리 > 계정관리 등록 View
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('SUPERVISOR')")
    @GetMapping(value = "/edit")
    public void edit(Model model) {
        model.addAttribute("accountInfo", new AccountInfo());
    }

    /**
     * 계정관리 > 계정관리 등록 > 기업명 검색
     * @param companyCode
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('SUPERVISOR')")
    @GetMapping(value = "/edit/existsCompany")
    @ResponseBody
    public Map<String, Object> selectCompany(String companyCode,  @AuthenticationPrincipal B2bManager currentManager) {
        return iAccountService.selectCompanyInfo(companyCode, currentManager);
    }

    /**
     * 계정관리 > 계정관리 등록 > 아이디 중복 확인
     * @param id
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('SUPERVISOR')")
    @GetMapping(value = "/edit/existsId")
    @ResponseBody
    public Map<String, Object> selectUserId(String id) {
        return iAccountService.selectUserId(id);
    }

    /**
     * 계정관리 > 계정관리 등록 > 휴대폰번호 중복 확인
     * @param telephone
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('SUPERVISOR')")
    @GetMapping(value = "/edit/existsTelephone")
    @ResponseBody
    public Map<String, Object> selectTelephone(String telephone) {
        return iAccountService.selectTelephone(telephone);
    }


    /**
     * 계정관리 > 계정관리 등록 > 계약의 추가,삭제에 따른 관리계약건 조회
     * @param companyCd
     * @param serialNumber
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('SUPERVISOR')")
    @GetMapping(value = "/edit/realTimeBizContract")
    @ResponseBody
    List<ContractInfo> getRealTimeAccountConstractList(String companyCd, String[] serialNumber) {
        return iAccountService.getRealTimeAccountConstractList(companyCd, serialNumber);
    }

    /**
     * 계정등록
     * @param accountInfo
     * @param bindingResult
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('SUPERVISOR')")
    @PostMapping(value = "/edit/save")
    public String accountSave(@Valid AccountInfo accountInfo,
                              BindingResult bindingResult, Model model) {
        model.addAttribute("accountInfo", accountInfo);

        if (bindingResult.hasErrors()) {
            return "accounts/edit";
        }

        if(iAccountService.insertAccountManagerValidation(accountInfo, bindingResult, "").hasErrors()) {
            return "accounts/edit";
        }

        AccountInfo info = iAccountService.insertAccountManager(accountInfo);

        if(info != null) {
            return "redirect:/accounts";
        } else {
            model.addAttribute("error", "SMS 발송에 실패하였습니다. 잠시 후 다시 시도해 주세요.");
            return "accounts/edit";
        }

    }

    /**
     * 계정수정
     * @param accountInfo
     * @param bindingResult
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('SUPERVISOR')")
    @PostMapping(value = "/{mgrSeq}/save")
    public String accountModify(@Valid AccountInfo accountInfo,
                                BindingResult bindingResult, @PathVariable("mgrSeq") Long mgrSeq,
                                @AuthenticationPrincipal B2bManager currentManager, Model model) {

        BindingResult validation = iAccountService.insertAccountManagerValidation(accountInfo, bindingResult, "update");

        if (bindingResult.hasErrors() || validation.hasErrors()) {
            model.addAttribute("accountInfo", accountInfo);
            model.addAttribute("bizContract", iAccountService.getRealTimeAccountConstractList(accountInfo.getCompanyCd(), accountInfo.getSerialNumber()));
            model.addAttribute("contract", iAccountService.getUserChoiceAccountConstractList(accountInfo.getCompanyCd(), accountInfo.getSerialNumber()));
            model.addAttribute("authorityCd", iAccountService.getAuthorityList(accountInfo.getCompanyCd(), currentManager));
        }

        if (bindingResult.hasErrors()) {
            return "accounts/modify";
        }

        if(validation.hasErrors()) {
            return "accounts/modify";
        }

        iAccountService.updateAccountManager(accountInfo);

        Map<String, Object> map = iAccountService.selectAccountManager(mgrSeq);
        model.addAttribute("manager", map.get("manager"));
        model.addAttribute("contract", map.get("contract"));
        return "redirect:/accounts/{mgrSeq}";

    }


}
