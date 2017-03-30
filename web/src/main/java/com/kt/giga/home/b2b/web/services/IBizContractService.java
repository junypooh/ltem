package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.ContractInfo;
import com.kt.giga.home.b2b.domain.Role;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by DaDa on 2017-02-06.
 */
public interface IBizContractService {

    /**
     * 계약목록 검색조건에 따른 조회
     * @param manager
     * @param contractsState
     * @param contractsColumn
     * @param searchWord
     * @param sortName
     * @return
     */
    List<ContractInfo> selectBizContractsInSearchCurrentUser(B2bManager manager, String contractsState, String contractsColumn, String searchWord, String sortName);

    /**
     * 계약담당자 추가 > 계정추가
     *
     * @param serialNumber
     * @param mgrSeqs
     */
    void updateMgrContractRel(String serialNumber, String[] mgrSeqs);

    /**
     * 계약 담당자 조회
     *
     * @return
     */
    List<B2bManager> selectManagersToBeInCharge(String serialNumber, String searchWord, Role currentUsersRole);

    /**
     * 계약명 수정
     *
     * @param serialNumber
     * @param contractName
     */
    void updateBizContractName(String serialNumber, String contractName);

    /**
     * 세션에 최근 본 계약 저장
     *
     * @param toContractInfo
     */
    void saveRecentlyViewed(ContractInfo toContractInfo);

    /**
     * 서비스계약 목록 조회
     *
     * @param serialNumber
     * @param statusCode
     * @param searchKey
     * @param searchText
     * @param pageable
     * @return
     */
    Map<String, Object> getDetail(String serialNumber, String statusCode, String searchKey, String searchText, Pageable pageable);

    /**
     * 현재 로그인된 매니저가 비즈 계약 담당인지 여부
     *
     * @param serialNumber
     * @return
     */
    Boolean isInChargeOf(String serialNumber);

    /**
     * 계약 담당자 추가 목록 > 가려진정보 해제 후 리스트 조회
     * @param mgrSeq
     * @return
     */
    B2bManager getUnmaskedContractInCharge(Long mgrSeq);

    /**
     * 전체 계약 담당자 > 가려진정보 해제 후 리스트 조회
     * @param mgrSeq
     * @return
     */
    B2bManager getUnmaskedContractAllInCharge(Long mgrSeq);


}
