package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.B2bManager;

import java.util.Map;

/**
 * Created by DaDa on 2017-03-15.
 */
public interface IManagerTermService {

    /**
     * 약관 동의 여부
     * @param b2bManager
     * @return
     */
    Boolean isManagerTermsCheck(B2bManager b2bManager);

    /**
     * 동의하지 않은 약관 조회
     * @param b2bManager
     * @return
     */
    Map<String, Object> selectManagerTermsCheck(B2bManager b2bManager);

    /**
     * 약관 동의 저장
     * @param b2bManagers
     * @param termSeqs
     */
    void managerTermsSave(B2bManager b2bManagers, Integer[] termSeqs);

}
