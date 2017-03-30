package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.CompanyInfo;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Created by junypooh on 2017-02-28.
 * <pre>
 * com.kt.giga.home.b2b.web.services.ICompanyService
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-02-28 오후 12:18
 */
public interface ICompanyService {

    /**
     * 기업 목록 조회
     * @param manager
     * @param pageable
     * @param column
     * @param searchWord
     * @return
     */
    Map<String, Object> selectCompanies(B2bManager manager, Pageable pageable, String column, String searchWord);

    /**
     * 기업 상세 조회
     * @param code
     * @return
     */
    CompanyInfo selectCompanyInfo(String code);

    /**
     * 기업 상세 조회
     * @param code
     * @return
     */
    CompanyInfo selectMaskedCompanyInfo(String code);

    /**
     * 기업 상세 조회
     * @param code
     * @return
     */
    CompanyInfo selectUnmaskedCompanyInfo(String code);

    /**
     * 기업 정보 수정
     * @param companyInfo
     */
    void updateCompanyInfo(CompanyInfo companyInfo);
}
