package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.SvcContractInfo;
import com.kt.giga.home.b2b.domain.UserInformation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * com.kt.giga.home.b2b.web.services
 * <p>
 * Created by cecil on 2017. 2. 25..
 */
public interface IServiceContractService {

    /**
     * 서비스계약 목록 조회 (엑셀 다운로드)
     *
     * @param svcContSerials
     * @return
     */
    List<SvcContractInfo> getSvcContractInfosToExcel(List<Long> svcContSerials);

    /**
     * 서비스계약 목록 조회 마스킹 해제
     *
     * @return
     */
    SvcContractInfo getUnmaskedSvcContractInfos(Long svcContSerial);

    /**
     * 서비스계약 상세 조회
     *
     * @param svcContSerial
     * @return
     */
    SvcContractInfo getMaskedServiceContractInfo(Long svcContSerial);

    /**
     * 서비스계약 상세 조회
     *
     * @param svcContSerial
     * @return
     */
    SvcContractInfo getUnmaskedServiceContractInfo(Long svcContSerial);

    /**
     * 서비스계약 상세 수정
     *
     *
     * @param serviceContractSerial
     * @param userInformation @throws RuntimeException
     */
    void updateSvcContractInfo(Long serviceContractSerial, UserInformation userInformation) throws RuntimeException;

    Page getServiceContracts(String serialNumber, String statusCode, String searchKey, String searchText, Pageable pageable);

    void unmapUser(Long serviceContractSerial);

    void resendUserAuthentication(Long serviceContractNo);
}
