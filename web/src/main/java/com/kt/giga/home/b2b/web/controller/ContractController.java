package com.kt.giga.home.b2b.web.controller;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.ContractInfo;
import com.kt.giga.home.b2b.domain.SvcContractInfo;
import com.kt.giga.home.b2b.entity.Manager;
import com.kt.giga.home.b2b.entity.ServiceContract;
import com.kt.giga.home.b2b.util.PageUtil;
import com.kt.giga.home.b2b.web.services.IAccountService;
import com.kt.giga.home.b2b.web.services.IBizContractService;
import com.kt.giga.home.b2b.web.services.ICommonService;
import com.kt.giga.home.b2b.web.services.IServiceContractService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DaDa on 2017-02-06.
 */
@Controller
@RequestMapping("/contracts")
@Slf4j
public class ContractController {

    @Autowired
    private IBizContractService bizContractService;

    @Autowired
    private IServiceContractService serviceContractService;

    @Autowired
    private PageUtil pageUtil;

    @Autowired
    private ICommonService icommonService;

    @Autowired
    private IAccountService iAccountService;

    /**
     * 계약관리 목록
     *
     * @param model
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('ROLE_STAFF')")
    @GetMapping
    public String list(Model model, @AuthenticationPrincipal B2bManager currentManager) {

        List<ContractInfo> contractInfos = bizContractService.selectBizContractsInSearchCurrentUser(currentManager, null, null, null, "created");

        if (contractInfos != null && contractInfos.size() == 1) {
            String[] values = this.getClass().getAnnotation(RequestMapping.class).value();
            return "redirect:" + values[0] + "/" + contractInfos.get(0).getSerialNumber();
        } else {
            model.addAttribute("bizContract", contractInfos);
            return "contracts/list";
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

        rtnMap.put("searchList", bizContractService.selectBizContractsInSearchCurrentUser(currentManager, contractsState, contractsColumn, searchWord, sortName));
        return rtnMap;
    }

    /**
     * 계약관리 목록 상단 박스 영역 사용
     *
     * @param contractsColumn
     * @param searchWord
     * @param sortName
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('ROLE_STAFF')")
    @GetMapping(value = "list.json")
    @ResponseBody
    public List<ContractInfo> listJson(String contractsColumn, String searchWord, String sortName, @AuthenticationPrincipal B2bManager currentManager) {

        if (StringUtils.isBlank(sortName)) {
            sortName = "created";
        }

        return bizContractService.selectBizContractsInSearchCurrentUser(currentManager, null, contractsColumn, searchWord, sortName);
    }

    /**
     * 계약관리 > 서비스계약 목록
     *
     * @param serialNumber 계약 번호
     * @param model
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or (hasRole('ROLE_STAFF') and (@bizContractService.isInChargeOf(#serialNumber)))")
    @GetMapping(value = "/{serialNumber}")
    public String serviceContractList(@PathVariable("serialNumber") String serialNumber
            , @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC, size = 30) Pageable pageable
            , String statusCode
            , String searchKey
            , String searchText
            , Model model) {

        Map<String, Object> map = bizContractService.getDetail(serialNumber, statusCode, searchKey, searchText, pageable);

        List<SvcContractInfo> svcContracts   = (List<SvcContractInfo>) map.get("contents");
        Page<ServiceContract> page           = (Page<ServiceContract>) map.get("page");
        ContractInfo          toContractInfo = (ContractInfo) map.get("contractInfo");

        model.addAttribute("contractInfo", toContractInfo);
        model.addAttribute("svcContracts", svcContracts);
        model.addAttribute("pageHtml", pageUtil.makePageHtml(page));
        model.addAttribute("page", page);

        // 최근 본 계약 세션 저장.
        bizContractService.saveRecentlyViewed(new ContractInfo(toContractInfo.getSerialNumber(), toContractInfo.getName()));

        return "contracts/svcList";
    }

    /**
     * 계약관리 > 서비스 계약 목록 > 가려진 정보 해제
     *
     * @param password
     * @param svcContSerial
     * @param serialNumber
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or (hasRole('ROLE_STAFF') and (@bizContractService.isInChargeOf(#serialNumber)))")
    @PostMapping("unmask")
    @ResponseBody
    public Object unmaskServiceContract(String password, Long svcContSerial, String serialNumber
            , @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC) Pageable pageable
            , String statusCode
            , String searchKey
            , String searchText
            , @AuthenticationPrincipal B2bManager currentManager) {

        Map<String, Object> rtnMap = new HashMap<>();

        if (icommonService.doesPasswordMatch(password, currentManager.getPassword())) {

            Map<String, Object> map = bizContractService.getDetail(serialNumber, statusCode, searchKey, searchText, pageable);

            rtnMap.put("originalList", map.get("contents"));
            rtnMap.put("info", serviceContractService.getUnmaskedSvcContractInfos(svcContSerial));

            rtnMap.put("errorCd", 0);
        } else {

            rtnMap.put("errorCd", -1);
            rtnMap.put("msg", "비밀번호가 일치하지 않습니다.");
        }

        return rtnMap;
    }


    /**
     * 계약명 수정
     *
     * @param serialNumber
     * @param contractName
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or (hasRole('ROLE_STAFF') and (@bizContractService.isInChargeOf(#serialNumber)))")
    @PostMapping("editContractName")
    @ResponseBody
    public Object editContractName(String serialNumber, String contractName) {

        bizContractService.updateBizContractName(serialNumber, contractName);

        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("errorCd", 0);

        return rtnMap;
    }

    /**
     * 계약관리 > 서비스계약 목록 > 엑셀 다운로드
     *
     * @param contractInfo
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or (hasRole('ROLE_STAFF') and (@bizContractService.isInChargeOf(#contractInfo.serialNumber)))")
    @PostMapping("svcListExcel")
    public ModelAndView selectedServiceContractsToExcel(ContractInfo contractInfo, Model model, @AuthenticationPrincipal B2bManager currentManager) {

        List<SvcContractInfo> svcContractInfos = serviceContractService.getSvcContractInfosToExcel(contractInfo
                .getSvcContSerials());

        //파일명
        model.addAttribute("fileName", LocalDate.now().toString() + "_계약관리_서비스계약_목록.xls");
        model.addAttribute("domains", svcContractInfos);

        return new ModelAndView("excelView");
    }

    /**
     * 해당 계약의 담당자리스트 조회
     *
     * @param model
     * @param pageable
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('SUPERVISOR')")
    @GetMapping(value = "/{serialNumber}/managers")
    public String contractAccountManager(Model model, @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC, size = 30) Pageable pageable,
                                         @PathVariable("serialNumber") String serialNumber) {

        Map<String, Object> map         = iAccountService.getContractManagerlist(serialNumber, pageable, null, null, null);
        List<B2bManager>    managerList = (List<B2bManager>) map.get("list");
        Page<Manager>       page        = (Page<Manager>) map.get("page");


        model.addAttribute("serialNumber", serialNumber);
        model.addAttribute("managerList", managerList);
        model.addAttribute("pageHtml", pageUtil.makePageHtml(page));
        model.addAttribute("pageNumber", page.getNumber());
        model.addAttribute("pageTotalCnt", page.getTotalElements());

        return "contracts/mgrList";
    }

    /**
     * 해당 계약의 담당자리스트 검색 Ajax
     *
     * @param pageable
     * @param status
     * @param column
     * @param searchWord
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('SUPERVISOR')")
    @GetMapping(value = "/managers/search")
    @ResponseBody
    public Object contractAccountManagerSearch(@PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC, size = 30) Pageable pageable
            , String serialNumber
            , String status, String column, String searchWord, @AuthenticationPrincipal B2bManager currentManager) {

        Map<String, Object> map         = iAccountService.getContractManagerlist(serialNumber, pageable, status, column, searchWord);
        List<B2bManager>    managerList = (List<B2bManager>) map.get("list");
        Page<Manager>       page        = (Page<Manager>) map.get("page");

        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("managerList", managerList);
        rtnMap.put("pageHtml", pageUtil.makePageHtml(page));
        rtnMap.put("pageNumber", page.getNumber());
        rtnMap.put("pageTotalCnt", page.getTotalElements());

        return rtnMap;
    }

    /**
     * 해당 계약중 담당자가 포함되지 않은 회사 managers 조회
     *
     * @param model
     * @param pageable
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('SUPERVISOR')")
    @GetMapping(value = "/{companyCd}/{serialNumber}/managers")
    public String contractAccountManagerAdd(Model model, @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC, size = 30) Pageable pageable,
                                            @PathVariable("serialNumber") String serialNumber,
                                            @PathVariable("companyCd") String companyCd,
                                            @AuthenticationPrincipal B2bManager currentManager) {

        Map<String, Object> map         = iAccountService.getContractManagerAddlist(currentManager, serialNumber, companyCd, pageable, null, null, null);
        List<B2bManager>    managerList = (List<B2bManager>) map.get("list");
        Page<Manager>       page        = (Page<Manager>) map.get("page");

        model.addAttribute("serialNumber", serialNumber);
        model.addAttribute("companyCd", companyCd);
        model.addAttribute("managerList", managerList);
        model.addAttribute("pageHtml", pageUtil.makePageHtml(page));
        model.addAttribute("pageNumber", page.getNumber());
        model.addAttribute("pageTotalCnt", page.getTotalElements());

        return "contracts/mgrAddList";
    }

    /**
     * 해당 계약중 담당자가 포함되지 않은 회사 managers 검색 Ajax
     *
     * @param pageable
     * @param status
     * @param column
     * @param searchWord
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('SUPERVISOR')")
    @GetMapping(value = "/managers/add/search")
    @ResponseBody
    public Object contractAccountManagerAddSearch(@PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC, size = 30) Pageable pageable
            , String serialNumber, String companyCd
            , String status, String column, String searchWord, @AuthenticationPrincipal B2bManager currentManager) {

        Map<String, Object> map         = iAccountService.getContractManagerAddlist(currentManager, serialNumber, companyCd, pageable, status, column, searchWord);
        List<B2bManager>    managerList = (List<B2bManager>) map.get("list");
        Page<Manager>       page        = (Page<Manager>) map.get("page");

        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("managerList", managerList);
        rtnMap.put("pageHtml", pageUtil.makePageHtml(page));
        rtnMap.put("pageNumber", page.getNumber());
        rtnMap.put("pageTotalCnt", page.getTotalElements());

        return rtnMap;
    }

    /**
     * 계약담당자 추가 > 계정추가
     *
     * @param serialNumber
     * @param mgrSeqs
     * @return
     */
    @PostMapping(value = "/managers/add")
    @ResponseBody
    public Object addMgrContractRel(String serialNumber, String[] mgrSeqs) {
        Map<String, Object> rtnMap = new HashMap<>();
        bizContractService.updateMgrContractRel(serialNumber, mgrSeqs);

        return rtnMap;
    }


    /**
     * 계약 담당자 추가 목록 > 가려진 정보 해제
     *
     * @param password
     * @param mgrSeq
     * @param currentManager
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('SUPERVISOR')")
    @PostMapping(value = "/inCharge/unmask")
    @ResponseBody
    public Object unmaskContractInChargeAdd(String password, Long mgrSeq, @AuthenticationPrincipal B2bManager currentManager) {
        Map<String, Object> rtnMap = new HashMap<>();

        if (icommonService.doesPasswordMatch(password, currentManager.getPassword())) {
            rtnMap.put("manager", bizContractService.getUnmaskedContractInCharge(mgrSeq));
            rtnMap.put("errorCd", 0);
        } else {
            rtnMap.put("errorCd", -1);
            rtnMap.put("msg", "비밀번호가 일치하지 않습니다.");
        }

        return rtnMap;
    }


    /**
     * 전체 계약 담당자 > 가려진 정보 해제
     *
     * @param password
     * @param mgrSeq
     * @param currentManager
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or hasRole('SUPERVISOR')")
    @PostMapping(value = "/allInCharge/unmask")
    @ResponseBody
    public Object unmaskAllContractInCharge(String password, Long mgrSeq, @AuthenticationPrincipal B2bManager currentManager) {
        Map<String, Object> rtnMap = new HashMap<>();

        if (icommonService.doesPasswordMatch(password, currentManager.getPassword())) {
            rtnMap.put("manager", bizContractService.getUnmaskedContractAllInCharge(mgrSeq));
            rtnMap.put("errorCd", 0);
        } else {
            rtnMap.put("errorCd", -1);
            rtnMap.put("msg", "비밀번호가 일치하지 않습니다.");
        }

        return rtnMap;
    }


}
