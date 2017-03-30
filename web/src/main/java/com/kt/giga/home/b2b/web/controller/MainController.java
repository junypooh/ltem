package com.kt.giga.home.b2b.web.controller;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.Role;
import com.kt.giga.home.b2b.web.services.IBannerService;
import com.kt.giga.home.b2b.web.services.ICompanyService;
import com.kt.giga.home.b2b.web.services.IMainService;
import com.kt.giga.home.b2b.web.services.INoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by junypooh on 2017-03-08.
 * <pre>
 * com.kt.giga.home.b2b.web.controller.MainController
 *
 * 메인 Controller
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-08 오후 5:09
 */
@Controller
@Slf4j
public class MainController {

    @Autowired
    private ICompanyService iCompanyService;

    @Autowired
    private IMainService iMainService;

    @Autowired
    private INoticeService iNoticeService;

    @Autowired
    private IBannerService iBannerService;

    @GetMapping(value = "")
    public String index(@AuthenticationPrincipal B2bManager b2bManager
            , @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC, size = 4) Pageable pageable
            , Model model) {

        model.addAttribute("companyInfo", iCompanyService.selectCompanyInfo(b2bManager.getCompanyCd()));
        model.addAttribute("avgCapacityOverInfo", iMainService.selectAvgCapacityOverInfo(b2bManager));

        if (b2bManager.getRole().equals(Role.SUPER_MASTER)) {

            // 계약 총 건수
            model.addAttribute("totContractCount", iMainService.selectContractsCount(b2bManager));
            // 기업 등록현황
            model.addAttribute("registCompanyCount", iMainService.selectRegistCompanyCount(b2bManager));
            // 계정 발급 현황
            model.addAttribute("registAccountCount", iMainService.selectRegistAccountCount(b2bManager));
            // 사용자 주소 미등록 건
            model.addAttribute("userAddrRegistCount", iMainService.selectUserAddrRegistCount(b2bManager));
            // 현재 허브 사용자
            model.addAttribute("hubCount", iMainService.selectHubCount(b2bManager));
            // 허브 연결 상태
            model.addAttribute("hubStatusCount", iMainService.selectHubStatusCount(b2bManager));

        } else if (b2bManager.getRole().equals(Role.MASTER) || b2bManager.getRole().equals(Role.SUPERVISOR)) {

            // 계약 총 건수(중복)
            model.addAttribute("totContractCount", iMainService.selectContractsCount(b2bManager));
            // 계정 발급 현황(중복)
            model.addAttribute("registAccountCount", iMainService.selectRegistAccountCount(b2bManager));
            // 담당자 미등록 계약 건
            model.addAttribute("notInChargeContractCount", iMainService.selectNotInChargeContractCount(b2bManager));
            // 사용자 주소 미등록 건(중복)
            model.addAttribute("userAddrRegistCount", iMainService.selectUserAddrRegistCount(b2bManager));
            // 현재 허브 사용자(중복)
            model.addAttribute("hubCount", iMainService.selectHubCount(b2bManager));
            // 허브 연결 상태(중복)
            model.addAttribute("hubStatusCount", iMainService.selectHubStatusCount(b2bManager));

        } else if (b2bManager.getRole().equals(Role.STAFF)) {

            // 계약 총 건수(중복)
            model.addAttribute("totContractCount", iMainService.selectContractsCount(b2bManager));
            // 계약 담당자 현황
            model.addAttribute("contrantInChargeStaffCount", iMainService.selectContrantInChargeStaffCount(b2bManager));
            // 담당자 미등록 계약 건(중복)
            model.addAttribute("notInChargeContractCount", iMainService.selectNotInChargeContractCount(b2bManager));
            // 사용자 주소 미등록 건(중복)
            model.addAttribute("userAddrRegistCount", iMainService.selectUserAddrRegistCount(b2bManager));
            // 현재 허브 사용자(중복)
            model.addAttribute("hubCount", iMainService.selectHubCount(b2bManager));
            // 허브 연결 상태(중복)
            model.addAttribute("hubStatusCount", iMainService.selectHubStatusCount(b2bManager));

        }

        // 공지사항
        model.addAttribute("notices", iNoticeService.selectNoticeInfoIsActiveTrue(pageable).get("notices"));
        // 배너
        model.addAttribute("banners", iBannerService.selectCurrentTrueAndCategory((short) 2));

//        log.debug("계약 총 건수 {}", iMainService.selectContractsCount(b2bManager));
//        log.debug("기업 등록현황 {}", iMainService.selectRegistCompanyCount(b2bManager));
//        log.debug("계정 발급 현황 {}", iMainService.selectRegistAccountCount(b2bManager));
//        log.debug("사용자 주소 미등록 건 {}", iMainService.selectUserAddrRegistCount(b2bManager));
//        log.debug("현재 허브 사용자 {}", iMainService.selectHubCount(b2bManager));
//        log.debug("허브 연결 상태 {}", iMainService.selectHubStatusCount(b2bManager));
//        log.debug("담당자 미등록 계약 건 {}", iMainService.selectNotInChargeContractCount(b2bManager));
//        log.debug("계약 담당자 현황 {}", iMainService.selectContrantInChargeStaffCount(b2bManager));

        /**
         * 슈퍼마스터
         */
        // 계약 총 건수
        // 기업 등록현황
        // 계정 발급 현황
        // 사용자 주소 미등록 건
        // 현재 허브 사용자
        // 허브 연결 상태

        /**
         * 마스터/슈퍼바이저
         */
        // 계약 총 건수(중복)
        // 계정 발급 현황(중복)
        // 담당자 미등록 계약 건
        // 사용자 주소 미등록 건(중복)
        // 현재 허브 사용자(중복)
        // 허브 연결 상태(중복)

        /**
         * 스탭
         */
        // 계약 총 건수(중복)
        // 계약 담당자 현황
        // 담당자 미등록 계약 건(중복)
        // 사용자 주소 미등록 건(중복)
        // 현재 허브 사용자(중복)
        // 허브 연결 상태(중복)

        /**
         * 개발 단위 리스트
         */
        // 계약 총 건수
        // 기업 등록현황
        // 계정 발급 현황
        // 사용자 주소 미등록 건
        // 현재 허브 사용자
        // 허브 연결 상태
        // 담당자 미등록 계약 건
        // 계약 담당자 현황

        return "index";

    }
}
