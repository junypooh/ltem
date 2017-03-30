package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.B2bManager;

import java.util.Map;

/**
 * Created by junypooh on 2017-03-09.
 * <pre>
 * com.kt.giga.home.b2b.web.services.IMainService
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-09 오후 5:26
 */
public interface IMainService {

    /**
     * 계약 총 건수
     * @param b2bManager
     * @return
     */
    long selectContractsCount(B2bManager b2bManager);

    /**
     * 기업 등록 현황(신규 등록 기업수 / 총 기업수)
     * @param b2bManager
     * @return
     */
    Map<String, Long> selectRegistCompanyCount(B2bManager b2bManager);

    /**
     * 계정 발급 현황
     * 권한 별 발급 건수
     * @param b2bManager
     * @return
     */
    Map<String, Long> selectRegistAccountCount(B2bManager b2bManager);

    /**
     * 사용자 주소 미등록 건
     * 주소 미등록 건, 주소 등록 건
     * @param b2bManager
     * @return
     */
    Map<String, Long> selectUserAddrRegistCount(B2bManager b2bManager);

    /**
     * 현재 허브 사용자
     * 인증 건수, 미인증 건수
     * @param b2bManager
     * @return
     */
    long selectHubCount(B2bManager b2bManager);

    /**
     * 허브 연결 상태(다른 schema)
     * @param b2bManager
     * @return
     */
    Map<String, Long> selectHubStatusCount(B2bManager b2bManager);

    /**
     * 담당자 미등록 계약 건
     * @param b2bManager
     * @return
     */
    Map<String, Long> selectNotInChargeContractCount(B2bManager b2bManager);

    /**
     * 계약 담당자 현황
     * 스탭의 경우만 사용함.
     * @param b2bManager
     * @return
     */
    long selectContrantInChargeStaffCount(B2bManager b2bManager);

    /**
     * 평균 허용량 초과 계약
     * @param b2bManager
     * @return
     */
    Object selectAvgCapacityOverInfo(B2bManager b2bManager);

    /**
     * 메인 공지사항
     * @return
     */
    Object selectMainNoticeInfo();
}
