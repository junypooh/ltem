package com.kt.giga.home.b2b.web.controller;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.kt.giga.home.b2b.cache.CompanyEdit;
import com.kt.giga.home.b2b.common.constant.CodeConstants;
import com.kt.giga.home.b2b.domain.AccountInfo;
import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.CompanyInfo;
import com.kt.giga.home.b2b.util.PageUtil;
import com.kt.giga.home.b2b.web.services.IAccountService;
import com.kt.giga.home.b2b.web.services.ICommonService;
import com.kt.giga.home.b2b.web.services.ICompanyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
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

/**
 * Created by junypooh on 2017-02-28.
 * <pre>
 * com.kt.giga.home.b2b.web.controller.CompanyController
 *
 * 계정관리 > 기업관리
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-02-28 오후 12:16
 */
@Controller
@RequestMapping("/company")
@Slf4j
public class CompanyController {

    @Autowired
    private ICompanyService iCompanyService;

    @Autowired
    private ICommonService icommonService;

    @Autowired
    private IAccountService iAccountService;

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Autowired
    private PageUtil pageUtil;

    /**
     * 계정관리 > 기업 목록
     *
     * @param pageable
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @GetMapping(value = "")
    public String list(Model model,
                       @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC, size = 30) Pageable pageable
                       , @AuthenticationPrincipal B2bManager currentManager) {

        Map<String, Object> map = iCompanyService.selectCompanies(currentManager, pageable, null, null);
        List<CompanyInfo> list = (List<CompanyInfo>) map.get("list");
        Page<CompanyInfo> page = (Page<CompanyInfo>) map.get("page");

        model.addAttribute("list", list);
        model.addAttribute("pageHtml", pageUtil.makePageHtml(page));
        model.addAttribute("pageNumber", page.getNumber());
        model.addAttribute("pageTotalCnt", page.getTotalElements());

        return "company/list";
    }

    /**
     * 계정관리 > 기업 목록 검색 Ajax
     * @param pageable
     * @param column
     * @param searchWord
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @GetMapping(value = "listSearch")
    @ResponseBody
    public Object listSearch(@PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC, size = 30) Pageable pageable
                        , String column, String searchWord
                        , @AuthenticationPrincipal B2bManager currentManager) {

        Map<String, Object> map = iCompanyService.selectCompanies(currentManager, pageable, column, searchWord);
        List<CompanyInfo> list = (List<CompanyInfo>) map.get("list");
        Page<CompanyInfo> page = (Page<CompanyInfo>) map.get("page");

        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("list", list);
        rtnMap.put("pageHtml", pageUtil.makePageHtml(page));
        rtnMap.put("pageNumber", page.getNumber());
        rtnMap.put("pageTotalCnt", page.getTotalElements());

        return rtnMap;
    }

    /**
     * 기업 상세
     * 기업명이 없을 경우 바로 수정 페이지로 이동
     * 기업명이 있을 경우 상세 페이지로 이동(마스킹 정보)
     * @param code
     * @param currentManager
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @GetMapping(value = "/{code}")
    public String companyDetail(@PathVariable("code") String code, @AuthenticationPrincipal B2bManager currentManager, Model model) {

        CompanyInfo companyInfo = iCompanyService.selectCompanyInfo(code);
        model.addAttribute("companyInfo", companyInfo);

        if(StringUtils.isNotBlank(companyInfo.getName())) {
            return "/company/detail";
        } else {
            // 바로 수정페이지로 접근하기 때문에 인증 캐쉬 생성
            IMap<Object, Object> map = hazelcastInstance.getMap(CodeConstants.COMPANY_CACHE_MAP_NAME);
            map.put(currentManager.getMgrSeq(), new CompanyEdit(currentManager.getMgrSeq(), code), CodeConstants.CACHE_TIME_OUT, CodeConstants.CACHE_TIME_UNIT);
            return "/company/edit";
        }

    }

    /**
     * 기업 수정 페이지
     * @param code
     * @param currentManager
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @PostMapping(value = "/{code}")
    public String companyEdit(@PathVariable("code") String code, @AuthenticationPrincipal B2bManager currentManager, String password, Model model) {

        if (StringUtils.isNotBlank(password) && icommonService.doesPasswordMatch(password, currentManager.getPassword())) {

            model.addAttribute("companyInfo", iCompanyService.selectUnmaskedCompanyInfo(code));

            // 가려진 정보 해제 여부 cache 등록 (5분 expired)
            IMap<Object, Object> map = hazelcastInstance.getMap(CodeConstants.COMPANY_CACHE_MAP_NAME);
            map.put(currentManager.getMgrSeq(), new CompanyEdit(currentManager.getMgrSeq(), code), CodeConstants.CACHE_TIME_OUT, CodeConstants.CACHE_TIME_UNIT);
            return "/company/edit";

        } else {
            model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("doesNotPasswordMatched", true);
            model.addAttribute("companyInfo", iCompanyService.selectMaskedCompanyInfo(code));
            return "company/detail";

        }
    }

    /**
     * 기업 수정
     * @param code
     * @param currentManager
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @PostMapping(value = "/{code}/save")
    public String companySave(@PathVariable("code") String code, @AuthenticationPrincipal B2bManager currentManager
            , @Valid @ModelAttribute CompanyInfo companyInfo, BindingResult bindingResult, Model model) {

        IMap<Object, Object> map = hazelcastInstance.getMap(CodeConstants.COMPANY_CACHE_MAP_NAME);
        CompanyEdit cacheCompanyEdit = (CompanyEdit) map.get(currentManager.getMgrSeq());

        CompanyEdit companyEdit = new CompanyEdit(currentManager.getMgrSeq(), code);

        if (!companyEdit.equals(cacheCompanyEdit)) {
            throw new AccessDeniedException("잘못된 경로로 접근하였습니다.");
        }
        if (bindingResult.hasErrors()) {
            companyInfo.setManager(iCompanyService.selectUnmaskedCompanyInfo(code).getManager());
            return "/company/edit";
        }

        companyInfo.setCode(code);
        iCompanyService.updateCompanyInfo(companyInfo);

        String[] values = this.getClass().getAnnotation(RequestMapping.class).value();
        return "redirect:" + values[0] + "/" + code;
    }

    /**
     * 계정등록
     * @param accountInfo
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @PostMapping(value = "/manager/save")
    @ResponseBody
    public Object accountSave(AccountInfo accountInfo) {

        Map<String, Object> map = new HashMap<>();

        if (iAccountService.existsMasterInCompany(accountInfo.getCompanyCd())) {

            map.put("errorCd", "02");
            map.put("msg", "이미 마스터 계정이 등록 되어 있습니다.");

            return map;
        }

        AccountInfo insertAccountManager = iAccountService.insertAccountManager(accountInfo);

        log.debug("{}", insertAccountManager);

        if(insertAccountManager != null) {
            map.put("errorCd", "00");
            map.put("manager", insertAccountManager);
        } else {
            map.put("errorCd", "01");
            map.put("msg", "SMS 발송에 실패하였습니다. 잠시 후 다시 시도해 주세요.");
        }

        return map;
    }
}
