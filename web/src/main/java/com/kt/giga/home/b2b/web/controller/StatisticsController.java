package com.kt.giga.home.b2b.web.controller;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.ContractInfo;
import com.kt.giga.home.b2b.domain.ContractUsageInfo;
import com.kt.giga.home.b2b.domain.StatisticsByHubInfo;
import com.kt.giga.home.b2b.util.PageUtil;
import com.kt.giga.home.b2b.web.services.IBizContractService;
import com.kt.giga.home.b2b.web.services.ICommonService;
import com.kt.giga.home.b2b.web.services.IStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by junypooh on 2017-03-15.
 * <pre>
 * com.kt.giga.home.b2b.web.controller.StatisticsController
 *
 * 통계관리 Controller
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-15 오후 4:17
 */
@Controller
@RequestMapping("/statistics")
@Slf4j
public class StatisticsController {

    @Autowired
    private IStatisticsService iStatisticsService;

    @Autowired
    private IBizContractService bizContractService;

    @Autowired
    private PageUtil pageUtil;

    @Autowired
    private ICommonService icommonService;

    /**
     * 통계관리 > 계약관리 목록
     *
     * @param model
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('ROLE_STAFF')")
    @GetMapping
    public String list(Model model, @AuthenticationPrincipal B2bManager currentManager) {

        List<ContractInfo> contractInfos = iStatisticsService.selectBizContracts(currentManager, null, null, null, "created");

        if (contractInfos != null && contractInfos.size() == 1) {
            String[] values = this.getClass().getAnnotation(RequestMapping.class).value();
            return "redirect:" + values[0] + "/" + contractInfos.get(0).getSerialNumber() + "/" + LocalDate.now().getYear();
        } else {
            model.addAttribute("bizContract", contractInfos);
            return "statistics/list";
        }

    }

    /**
     * 계약관리 목록 검색 ajax
     *
     * @param contractsState
     * @param contractsColumn
     * @param searchWord
     * @param sortName
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('ROLE_STAFF')")
    @GetMapping(value = "listSearch")
    @ResponseBody
    public Object listSearch(String contractsState, String contractsColumn, String searchWord, String sortName, @AuthenticationPrincipal B2bManager currentManager) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("searchList", iStatisticsService.selectBizContracts(currentManager, contractsState, contractsColumn, searchWord, sortName));
        return rtnMap;
    }

    /**
     * 통계관리 > 전체통계
     * @param serialNumber
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('ROLE_STAFF')")
    @GetMapping(value = "/total/{serialNumber}/{year}")
    public String totalStatistics(@PathVariable("serialNumber") String serialNumber, @PathVariable("year") String year, Model model) {

        Map<String, Object> map = iStatisticsService.selectTotalUsage(serialNumber, year);

        ContractInfo toContractInfo = (ContractInfo) map.get("contractInfo");

        model.addAttribute("contractInfo", toContractInfo);
        model.addAttribute("contractMonthUsages", map.get("contractMonthUsages"));
        model.addAttribute("targetYear", year);

        // 최근 본 계약 세션 저장.
        bizContractService.saveRecentlyViewed(new ContractInfo(toContractInfo.getSerialNumber(), toContractInfo.getName()));

        return "statistics/totalUsage";
    }

    /**
     * 통계관리 > 전체통계 > 엑셀 다운로드
     *
     * @param contractUsageInfo
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('ROLE_STAFF')")
    @PostMapping(value = "totalUsageExcel")
    public ModelAndView totalStatisticsToExcel(ContractUsageInfo contractUsageInfo, Model model, @AuthenticationPrincipal B2bManager currentManager) {

        List<ContractUsageInfo> contractInfos = iStatisticsService.selectTotalUsageToExcel(contractUsageInfo);

        //파일명
        model.addAttribute("fileName", LocalDate.now().toString() + "_통계관리_전체통계.xls");
        model.addAttribute("domains", contractInfos);

        return new ModelAndView("excelView");
    }

    /**
     * 통계관리 > 허브별 통계
     * @param serialNumber
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('ROLE_STAFF')")
    @GetMapping(value = "/hub/{serialNumber}")
    public String hubStatistics(@PathVariable("serialNumber") String serialNumber, Integer page, Model model) {

        if(page == null)
            page = 0;

        Map<String, Object> map = iStatisticsService.selectHubUsage(serialNumber, page);

        model.addAttribute("contractInfo", map.get("contractInfo"));
        model.addAttribute("statisticsByHubInfos", map.get("statisticsByHubInfos"));
        model.addAttribute("pageHtml", pageUtil.makePageHtml(pageUtil.getDefaultPageCount(), (int)map.get("totCount"), page, 30));
        model.addAttribute("page", page);

        return "statistics/hubUsage";
    }

    /**
     * 통계관리 > 허브별 통계 > 마스크 해제
     * @param serialNumber
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('ROLE_STAFF')")
    @PostMapping(value = "/hub/unmask/{serialNumber}/{svcContSerial}")
    @ResponseBody
    public Map<String, Object> hubStatisticUnmask(@PathVariable("serialNumber") String serialNumber, @PathVariable("svcContSerial") Long svcContSerial, Integer page
            , String password
            , @AuthenticationPrincipal B2bManager currentManager
            , Model model) {

        Map<String, Object> rtnMap = new HashMap<>();

        if (icommonService.doesPasswordMatch(password, currentManager.getPassword())) {

            if(page == null)
                page = 0;

            Map<String, Object> map = iStatisticsService.selectHubUsageUnMask(serialNumber, svcContSerial, page);

            rtnMap.putAll(map);
            rtnMap.put("errorCd", 0);
        } else {

            rtnMap.put("errorCd", -1);
            rtnMap.put("msg", "비밀번호가 일치하지 않습니다.");
        }

        return rtnMap;
    }

    /**
     * 통계관리 > 허브별 통계 > 엑셀 다운로드
     *
     * @param statisticsByHubInfo
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('ROLE_STAFF')")
    @PostMapping(value = "hubUsageExcel")
    public ModelAndView hubUsageExcel(StatisticsByHubInfo statisticsByHubInfo, Model model, @AuthenticationPrincipal B2bManager currentManager) {

        List<StatisticsByHubInfo> statisticsByHubInfos = iStatisticsService.selectHubUsageToExcel(statisticsByHubInfo);

        //파일명
        model.addAttribute("fileName", LocalDate.now().toString() + "_통계관리_허브별통계.xls");
        model.addAttribute("domains", statisticsByHubInfos);

        return new ModelAndView("excelView");
    }
}
