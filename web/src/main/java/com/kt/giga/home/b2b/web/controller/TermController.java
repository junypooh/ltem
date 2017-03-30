package com.kt.giga.home.b2b.web.controller;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.TermInfo;
import com.kt.giga.home.b2b.entity.Term;
import com.kt.giga.home.b2b.util.PageUtil;
import com.kt.giga.home.b2b.web.services.ICommonService;
import com.kt.giga.home.b2b.web.services.ITermService;
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

/**
 * Created by DaDa on 2017-03-14.
 */
@Controller
@RequestMapping("/term")
public class TermController {

    @Autowired
    private ITermService termService;

    @Autowired
    private PageUtil pageUtil;

    @Autowired
    private ICommonService commonService;

    /**
     * 서비스관리 > 서비스 이용약관 리스트
     * @param model
     * @param pageable
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @GetMapping(value = "")
    public String list(Model model, @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC, size = 30) Pageable pageable) {
        Map<String, Object> map = termService.selectTermInfoList(pageable);

        List<TermInfo> termInfoList = (List<TermInfo>) map.get("termInfoList");
        Page<Term> page = (Page<Term>) map.get("page");

        model.addAttribute("termList", termInfoList);

        model.addAttribute("pageHtml", pageUtil.makePageHtml(page));
        model.addAttribute("pageNumber", page.getNumber());
        model.addAttribute("pageTotalCnt", page.getTotalElements());

        return "/term/list";
    }


    /**
     * 서비스관리 > 서비스 이용약관 페이징 리스트 Ajax
     * @param model
     * @param pageable
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @GetMapping(value = "pageAjax")
    @ResponseBody
    public Map<String, Object> listAjax(Model model, @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC, size = 30) Pageable pageable) {
        Map<String, Object> rtnMap = new HashMap<>();

        Map<String, Object> map = termService.selectTermInfoList(pageable);

        List<TermInfo> termInfoList = (List<TermInfo>) map.get("termInfoList");
        Page<Term> page = (Page<Term>) map.get("page");


        rtnMap.put("termList", termInfoList);

        rtnMap.put("pageHtml", pageUtil.makePageHtml(page));
        rtnMap.put("pageNumber", page.getNumber());
        rtnMap.put("pageTotalCnt", page.getTotalElements());

        return rtnMap;
    }

    /**
     * 서비스관리 > 서비스 이용약관 등록 View
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @GetMapping(value = "/edit")
    public void edit() {}

    /**
     * 서비스관리 > 서비스 이용약관 등록
     * @param termInfo
     * @param bindingResult
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @PostMapping("/save")
    public String termSave(@Valid @ModelAttribute TermInfo termInfo, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "term/edit";
        }

        termService.insertTermInfo(termInfo);

        return "redirect:/term";
    }

    /**
     * 서비스관리 > 서비스 이용약관 상세, 수정 View
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @GetMapping(value = "/{termSeq}")
    public String detail(Model model, @PathVariable("termSeq") int termSeq) {
        model.addAttribute("termInfo", termService.selectTermInfoDetail(termSeq));
        return "term/modify";
    }

    /**
     * 서비스관리 > 서비스 이용약관 수정
     * @param termInfo
     * @param bindingResult
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @PostMapping("/{termSeq}/save")
    public String termUpdate(@Valid @ModelAttribute TermInfo termInfo, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "term/modify";
        }
        termService.insertTermInfo(termInfo);
        return "redirect:/term";
    }

    /**
     * 약관 삭제
     * @param termSeqs
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @PostMapping("/delete")
    @ResponseBody
    public void termDelete(Integer[] termSeqs) { termService.deleteTermInfo(termSeqs); }


    /**
     * 가려진 정보 해제
     * @param password
     * @param termSeq
     * @param currentManager
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @PostMapping(value = "/unmask")
    @ResponseBody
    public Object unmaskAccount(String password, Integer termSeq, @AuthenticationPrincipal B2bManager currentManager) {
        Map<String, Object> rtnMap = new HashMap<>();

        if(commonService.doesPasswordMatch(password, currentManager.getPassword())) {
            rtnMap.put("info", termService.selectUnmaskTermInfoDetail(termSeq));
            rtnMap.put("errorCd", 0);
        } else {
            rtnMap.put("errorCd", -1);
            rtnMap.put("msg", "비밀번호가 일치하지 않습니다.");
        }

        return rtnMap;
    }



}
