package com.kt.giga.home.b2b.web.controller;

import com.kt.giga.home.b2b.domain.*;
import com.kt.giga.home.b2b.web.services.IServiceContractService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.Map;

/**
 * com.kt.giga.home.b2b.web.controller
 * <p>
 * Created by cecil on 2017. 3. 23..
 */
@Slf4j
@Controller
@RequestMapping("/contracts/{serialNumber}/{svcContSerial}")
@SessionAttributes("UnmaskedContract")
public class ServiceContractController {

    @Autowired
    private IServiceContractService serviceContractService;

    @Autowired
    private StandardPasswordEncoder standardPasswordEncoder;

    @ModelAttribute("UnmaskedContract")
    public UnmaskedContract getUnmaskedContract() {
        return new UnmaskedContract();
    }

    /**
     * 계약관리 > 서비스계약 상세
     *
     * @param serialNumber  계약 번호
     * @param svcContSerial 서비스 계약 번호
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or (hasRole('ROLE_STAFF') and (@bizContractService.isInChargeOf(#serialNumber)))")
    @GetMapping
    public String get(@ModelAttribute("UnmaskedContract") UnmaskedContract unmaskedContract, @PathVariable("serialNumber") String serialNumber, @PathVariable("svcContSerial") Long svcContSerial
            , Model model, SessionStatus sessionStatus) {

        if (serialNumber.equals(unmaskedContract.getBizContractNumber()) && svcContSerial.equals(unmaskedContract.getServiceContractNumber())) {
            sessionStatus.setComplete();

            SvcContractInfo unmaskedServiceContractInfo = serviceContractService.getUnmaskedServiceContractInfo(svcContSerial);
            model.addAttribute("svcContractInfo", unmaskedServiceContractInfo);
            return "contracts/svcEdit";
        } else {
            model.addAttribute("svcContractInfo", serviceContractService.getMaskedServiceContractInfo(svcContSerial));
            return "contracts/svcDetail";
        }
    }

    /**
     * 계약관리 > 서비스계약 상세 > 마스크 해제
     *
     * @param serialNumber  계약 번호
     * @param svcContSerial 서비스 계약 번호
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or (hasRole('ROLE_STAFF') and (@bizContractService.isInChargeOf(#serialNumber)))")
    @PostMapping(value = "/unmask", produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    public Map<String, String> unmask(UnmaskedContract unmaskedContract, @PathVariable("serialNumber") String serialNumber, @PathVariable("svcContSerial") Long svcContSerial
            , @RequestBody UnmaskRequest unmaskRequest, Model model, @AuthenticationPrincipal B2bManager currentManager) {

        if (StringUtils.isNotBlank(unmaskRequest.getPassword()) && standardPasswordEncoder.matches(unmaskRequest.getPassword(), currentManager
                .getPassword())) {
            model.addAttribute("UnmaskedContract", new UnmaskedContract(serialNumber, svcContSerial));
            return Collections.singletonMap("code", "00");
        }

        return Collections.singletonMap("code", "-10");

    }

    /**
     * 계약관리 > 서비스계약 상세 > 저장
     *
     * @param
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or (hasRole('ROLE_STAFF') and (@bizContractService.isInChargeOf(#serialNumber)))")
    @PutMapping(produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    public Map<String, String> save(@PathVariable("serialNumber") String serialNumber
            , @PathVariable("svcContSerial") Long svcContSerial
            , @RequestBody UserInformation userInformation) {


        try {
            serviceContractService.updateSvcContractInfo(svcContSerial, userInformation);
        } catch (IllegalStateException e) {
            log.info("Failed to update service contract number : {}", svcContSerial, e);
            return Collections.singletonMap("code", "-10");
        } catch (RuntimeException e) {
            log.error("Failed to update service contract number : {}", svcContSerial, e);
            return Collections.singletonMap("code", "-20");
        }

        return Collections.singletonMap("code", "00");

    }

    /**
     * 계약관리 > 서비스계약 상세 > 사용자 해제
     *
     * @param serialNumber
     * @param svcContSerial
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or (hasRole('ROLE_STAFF') and (@bizContractService.isInChargeOf(#serialNumber)))")
    @PutMapping("/unmap")
    @ResponseBody
    public Map<String, String> unmap(@PathVariable("serialNumber") String serialNumber, @PathVariable("svcContSerial") Long svcContSerial) {

        try {
            serviceContractService.unmapUser(svcContSerial);
            return Collections.singletonMap("errorCd", "00");
        } catch (RuntimeException e) {
            log.error("Failed to unmap user from service contract number : {}", svcContSerial, e);
            return Collections.singletonMap("errorCd", "01");
        }

    }

    /**
     * 계약관리 > 서비스계약 상세 > 인증번호 재발송
     *
     * @param serialNumber
     * @param svcContSerial
     * @return
     */
    @PreAuthorize("hasRole('ROLE_SUPER_MASTER') or (hasRole('ROLE_STAFF') and (@bizContractService.isInChargeOf(#serialNumber)))")
    @PostMapping("/send")
    @ResponseBody
    public Object resendAuthenticationNumber(@PathVariable("serialNumber") String serialNumber
            , @PathVariable("svcContSerial") Long svcContSerial) {

        try {
            serviceContractService.resendUserAuthentication(svcContSerial);
            return Collections.singletonMap("errorCd", "00");
        } catch (Exception e) {
            return Collections.singletonMap("errorCd", "01");
        }

    }

}
