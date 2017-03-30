package com.kt.giga.home.b2b.web.controller;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.HistoryInfo;
import com.kt.giga.home.b2b.domain.Role;
import com.kt.giga.home.b2b.util.PageUtil;
import com.kt.giga.home.b2b.view.GenericExcelView;
import com.kt.giga.home.b2b.web.services.ICommonService;
import com.kt.giga.home.b2b.web.services.IHistoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DaDa on 2017-03-22.
 */
@Controller
@RequestMapping("history")
public class HistoryController {

    @Autowired
    private IHistoryService historyService;

    @Autowired
    private PageUtil pageUtil;

    @Autowired
    private ICommonService icommonService;



    /**
     * 이력관리 > 이력조회
     * @param page
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('ROLE_MASTER')")
    @GetMapping(value = "")
    public String list(Integer page, @AuthenticationPrincipal B2bManager currentManager, Model model) {

        // 마스터 일 경우 회사별 이력 조회 화면으로 바로 이동.
        if(currentManager.getRole().equals(Role.MASTER)) {
            String[] values = this.getClass().getAnnotation(RequestMapping.class).value();
            return "redirect:" + values[0] + "/" + currentManager.getCompanyCd();
        }

        if(page == null) page = 0;

        Map<String, Object> map = historyService.getHistoryInfo(page);
        model.addAttribute("historyList", map.get("historyList"));
        model.addAttribute("pageHtml", pageUtil.makePageHtml(pageUtil.getDefaultPageCount(), (int)map.get("totCount"), page, 30));
        model.addAttribute("page", page);
        return "history/list";
    }

    /**
     * 이력관리 > 이력조회 > 엑셀 다운로드
     *
     * @param historyInfo
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER')")
    @PostMapping(value = "historyExcel")
    public ModelAndView historyExcel(HistoryInfo historyInfo, Model model, @AuthenticationPrincipal B2bManager currentManager) throws Exception {

        List<HistoryInfo> historyInfos = historyService.getHistoryToExcel(historyInfo);

        ArrayList<Field> listHeaderField = new ArrayList<>();
        listHeaderField.add(GenericExcelView.getField(HistoryInfo.class, "companyName"));
        listHeaderField.add(GenericExcelView.getField(HistoryInfo.class, "maskedCount"));
        listHeaderField.add(GenericExcelView.getField(HistoryInfo.class, "downloadCount"));


        //파일명
        model.addAttribute("fileName", LocalDate.now().toString() + "_이력관리_이력조회.xls");
        model.addAttribute("domains", historyInfos);
        model.addAttribute("listHeaderField", listHeaderField);

        return new ModelAndView("excelView");
    }

    /**
     * 이력관리 > 회사별 이력 조회
     * @param companyCd
     * @param startYear
     * @param startMonth
     * @param endYear
     * @param endMonth
     * @param page
     * @param currentManager
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or ( hasRole('ROLE_MASTER') and #currentManager.companyCd.equals(#companyCd) )")
    @GetMapping(value = "/{companyCd}")
    public String historyList(@PathVariable("companyCd") String companyCd
                              , String startYear, String startMonth, String endYear, String endMonth
                              , Integer page, @AuthenticationPrincipal B2bManager currentManager, Model model) {

        if(StringUtils.isBlank(startYear) || StringUtils.isBlank(startMonth)) {
            startYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));
            startMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
        }
        if(StringUtils.isBlank(endYear) || StringUtils.isBlank(endMonth)) {
            endYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));
            endMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
        }

        if(page == null) page = 0;

        Map<String, Object> map = historyService.getHistoryInfoGroupByCompany(companyCd, startYear + startMonth, endYear + endMonth, page);

        model.addAttribute("rtnInfo", map);
        model.addAttribute("pageHtml", pageUtil.makePageHtml(pageUtil.getDefaultPageCount(), (int)map.get("totCount"), page, 30));
        model.addAttribute("page", page);
        model.addAttribute("startYear", startYear);
        model.addAttribute("startMonth", startMonth);
        model.addAttribute("endYear", endYear);
        model.addAttribute("endMonth", endMonth);

        return "history/company";
    }

    /**
     * 이력관리 > 회사별 이력 조회 > 마스킹 해제
     * @param companyCd
     * @param password
     * @param startYear
     * @param startMonth
     * @param endYear
     * @param endMonth
     * @param managerUserId
     * @param page
     * @param currentManager
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or ( hasRole('ROLE_MASTER') and #currentManager.companyCd.equals(#companyCd) )")
    @PostMapping(value = "/{companyCd}/unmask")
    @ResponseBody
    public Map<String, Object> historyListUnmask(@PathVariable("companyCd") String companyCd
                                    , String password, String startYear, String startMonth, String endYear, String endMonth, String managerUserId
                                    , Integer page, @AuthenticationPrincipal B2bManager currentManager) {

        Map<String, Object> rtnMap = new HashMap<>();

        if (icommonService.doesPasswordMatch(password, currentManager.getPassword())) {

            rtnMap.putAll(historyService.getHistoryInfoGroupByUnmaskCompany(companyCd, managerUserId, startYear + startMonth, endYear + endMonth, page));
            rtnMap.put("errorCd", 0);
        } else {

            rtnMap.put("errorCd", -1);
            rtnMap.put("msg", "비밀번호가 일치하지 않습니다.");
        }

        return rtnMap;
    }

    /**
     * 이력관리 > 회사별 이력 조회 > 엑셀 다운로드
     *
     * @param historyInfo
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or ( hasRole('ROLE_MASTER') and #currentManager.companyCd.equals(#companyCd) )")
    @PostMapping(value = "/{companyCd}/excel")
    public ModelAndView companyHistoryExcel(@PathVariable("companyCd") String companyCd
                                            , HistoryInfo historyInfo
                                            , String startYear, String startMonth, String endYear, String endMonth
                                            , Model model, @AuthenticationPrincipal B2bManager currentManager) throws Exception {

        List<HistoryInfo> historyInfos = historyService.getCompanyHistoryToExcel(historyInfo, companyCd, startYear + startMonth, endYear + endMonth);

        ArrayList<Field> listHeaderField = new ArrayList<>();
        listHeaderField.add(GenericExcelView.getField(HistoryInfo.class, "managerMaskUserId"));
        listHeaderField.add(GenericExcelView.getField(HistoryInfo.class, "managerMaskName"));
        listHeaderField.add(GenericExcelView.getField(HistoryInfo.class, "authorityName"));
        listHeaderField.add(GenericExcelView.getField(HistoryInfo.class, "maskedCount"));
        listHeaderField.add(GenericExcelView.getField(HistoryInfo.class, "downloadCount"));
        listHeaderField.add(GenericExcelView.getField(HistoryInfo.class, "lastHistoryDate"));

        //파일명
        model.addAttribute("fileName", LocalDate.now().toString() + "_이력관리_회사별이력조회.xls");
        model.addAttribute("domains", historyInfos);
        model.addAttribute("listHeaderField", listHeaderField);

        return new ModelAndView("excelView");
    }

    /**
     * 이력관리 > 기업별 상세 목록 > 상세
     * @param model
     * @param managerUserId
     * @param startDt
     * @param endDt
     * @param page
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('ROLE_MASTER')")
    @GetMapping(value = "/manager")
    public String managerHistory(Model model, String managerUserId, String startDt, String endDt, Integer page) {
        if (page == null) page = 0;

        Map<String, Object> map = historyService.getHistoryInfoGroupByManager(managerUserId, startDt, endDt, page);
        model.addAttribute("managerHistory", map.get("managerHistory"));
        model.addAttribute("historyList", map.get("historyList"));
        model.addAttribute("pageHtml", pageUtil.makePageHtml(pageUtil.getDefaultPageCount(), (int)map.get("totCount"), page, 30));
        model.addAttribute("page", page);

        return "history/manager";
    }

    /**
     * 이력관리 > 기업별 상세 목록 > 상세리스트 페이징
     * @param managerUserId
     * @param startDt
     * @param endDt
     * @param page
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('ROLE_MASTER')")
    @GetMapping(value = "/manager/pageAjax")
    @ResponseBody
    public Map<String, Object> managerHistoryPaging(String managerUserId, String startDt, String endDt, Integer page) {
        if (page == null) page = 0;

        Map<String, Object> rtnMap = new HashMap<>();

        Map<String, Object> map = historyService.getHistoryInfoGroupByManager(managerUserId, startDt, endDt, page);
        rtnMap.put("managerHistory", map.get("managerHistory"));
        rtnMap.put("historyList", map.get("historyList"));
        rtnMap.put("pageHtml", pageUtil.makePageHtml(pageUtil.getDefaultPageCount(), (int)map.get("totCount"), page, 30));
        rtnMap.put("page", page);

        return rtnMap;
    }

    /**
     * 이력관리 > 기업별 상세 목록 > 상세 > 엑셀 다운로드
     *
     * @param historyInfo
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('ROLE_MASTER')")
    @PostMapping(value = "managerExcel")
    public ModelAndView managerExcel(HistoryInfo historyInfo, Model model) throws Exception {

        List<HistoryInfo> historyInfos = historyService.getHistoryGroupByManagerToExcel(historyInfo);

        ArrayList<Field> listHeaderField = new ArrayList<>();
        listHeaderField.add(GenericExcelView.getField(HistoryInfo.class, "bizContSerial"));
        listHeaderField.add(GenericExcelView.getField(HistoryInfo.class, "menuPath"));
        listHeaderField.add(GenericExcelView.getField(HistoryInfo.class, "userName"));
        listHeaderField.add(GenericExcelView.getField(HistoryInfo.class, "targetInfo"));
        listHeaderField.add(GenericExcelView.getField(HistoryInfo.class, "workContext"));
        listHeaderField.add(GenericExcelView.getField(HistoryInfo.class, "workHistoryDate"));

        //파일명
        model.addAttribute("fileName", LocalDate.now().toString() + "_이력관리_담당자별이력조회.xls");
        model.addAttribute("domains", historyInfos);
        model.addAttribute("listHeaderField", listHeaderField);

        return new ModelAndView("excelView");
    }

    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('ROLE_MASTER')")
    @PostMapping(value = "/manager/unmask")
    @ResponseBody
    public Map<String, Object> managerUnmaskHistory(Integer historySeq, String password, @AuthenticationPrincipal B2bManager currentManager) {


        Map<String, Object> rtnMap = new HashMap<>();

        if (icommonService.doesPasswordMatch(password, currentManager.getPassword())) {
            rtnMap.put("historyInfo", historyService.getHistoryInfoGroupByUnmaskManager(historySeq));
            rtnMap.put("errorCd", 0);
        } else {
            rtnMap.put("errorCd", -1);
            rtnMap.put("msg", "비밀번호가 일치하지 않습니다.");
        }

        return rtnMap;
    }




}
