package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.TermInfo;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Created by DaDa on 2017-01-10.
 */
public interface ITermService {

    /**
     * 약관 리스트 조회
     * @return
     */
    Map<String, Object> selectTermInfoList(Pageable pageable);

    /**
     * 약관 상세 조회
     * @param termSeq
     * @return
     */
    TermInfo selectTermInfoDetail(int termSeq);

    TermInfo selectUnmaskTermInfoDetail(int termSeq);

    /**
     * 약관 등록
     * @param termInfo
     */
    void insertTermInfo(TermInfo termInfo);

    /**
     * 약관 삭제
     * @param termSeqs
     */
    void deleteTermInfo(Integer[] termSeqs);

    /**
     * 활성화된 약관중 카테고리 조건으로 조회
     * @param category
     * @return
     */
    TermInfo getIsActiveTrueAndCategor(Short category);

    /**
     * 카테고리 조건으로 조회
     * @param category
     * @return
     */
    Map<String, Object> getTermsCategor(Short category, Integer termSeq);




}
