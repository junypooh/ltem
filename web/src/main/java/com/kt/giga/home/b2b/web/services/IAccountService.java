package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.AccountInfo;
import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.ContractInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;

/**
 * Created by DaDa on 2017-02-23.
 */
public interface IAccountService {

    Map<String, Object> selectAccountManagers(B2bManager manager, Pageable pageable, String status, String column, String searchWord);

    Map<String, Object> deleteAccountConfirm(Long[] mgrSeqs);

    Map<String, Object> deleteAccountManagers(Long[] mgrSeqs);

    B2bManager getUnmaskedAccountManager(Long mgrSeq);

    B2bManager unmaskManager(Long mgrSeq);

    Map<String, Object> selectAccountManager(Long mgrSeq);

    Map<String, Object> selectUnmaskAccountManager(Long mgrSeq);

    Map<String, Object> selectCompanyInfo(String companyCd, B2bManager currentManager);

    List<Map<String,Object>> getAuthorityList(String companyCd, B2bManager currentManager);

    Map<String, Object> selectUserId(String id);

    Map<String, Object> selectTelephone(String telephone);

    List<ContractInfo> getRealTimeAccountConstractList(String companyCd, String[] serialNumber);

    List<ContractInfo> getUserChoiceAccountConstractList(String companyCd, String[] serialNumber);

    BindingResult insertAccountManagerValidation(AccountInfo accountInfo, BindingResult bindingResult, String type);

    AccountInfo insertAccountManager(AccountInfo accountInfo);

    Map<String, Object> getUnmaskedAccountManagerDetail(Long mgrSeq, String companyCd, B2bManager currentManager);

    void updateAccountManager(AccountInfo accountInfo);

    /**
     * 해당 계약의 담당자 리스트
     * @param serialNumber
     * @param pageable
     * @param status
     * @param column
     * @param searchWord
     * @return
     */
    Map<String, Object> getContractManagerlist(String serialNumber, Pageable pageable,  String status, String column, String searchWord);

    /**
     * 해당 회사의 회원들 중 계약 추가 대상인 리스트 조회
     * @param companyCd
     * @param pageable
     * @param status
     * @param column
     * @param searchWord
     * @return
     */
    Map<String, Object> getContractManagerAddlist(B2bManager manager, String serialNumber, String companyCd, Pageable pageable, String status, String column, String searchWord);

    /**
     * 특정 회사에 마스트 권한 계정 존재 여부
     * @param companyCd
     * @return
     */
    Boolean existsMasterInCompany(String companyCd);

}
