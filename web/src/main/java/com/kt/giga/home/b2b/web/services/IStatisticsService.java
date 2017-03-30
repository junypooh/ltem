package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.ContractInfo;
import com.kt.giga.home.b2b.domain.ContractUsageInfo;
import com.kt.giga.home.b2b.domain.StatisticsByHubInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by junypooh on 2017-03-15.
 * <pre>
 * com.kt.giga.home.b2b.web.services.IStatisticsService
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-15 오후 4:28
 */
public interface IStatisticsService {

    /**
     * 계약목록 검색조건에 따른 조회
     * @param manager
     * @param contractsState
     * @param contractsColumn
     * @param searchWord
     * @param sortName
     * @return
     */
    List<ContractInfo> selectBizContracts(B2bManager manager, String contractsState, String contractsColumn, String searchWord, String sortName);

    /**
     * 통계관리 > 전체 통계
     * @param serialNumber
     * @param year
     * @return
     */
    Map<String, Object> selectTotalUsage(String serialNumber, String year);

    /**
     * 통계관리 > 전체 통계 > 엑셀 다운로드
     * @param contractUsageInfo
     * @return
     */
    List<ContractUsageInfo> selectTotalUsageToExcel(ContractUsageInfo contractUsageInfo);

    /**
     * 통계관리 > 허브별 통계
     * @param serialNumber
     * @param page
     * @return
     */
    Map<String, Object> selectHubUsage(String serialNumber, int page);

    /**
     * 통계관리 > 허브별 통계 > 마스크 해제
     * @param serialNumber
     * @param page
     * @return
     */
    Map<String, Object> selectHubUsageUnMask(String serialNumber, Long svcContSerial, int page);

    StatisticsByHubInfo getStatisticsByHubInfo(String serialNumber, Long svcContSerial);

    /**
     * 통계관리 > 전체 통계 > 엑셀 다운로드
     * @param statisticsByHubInfo
     * @return
     */
    List<StatisticsByHubInfo> selectHubUsageToExcel(StatisticsByHubInfo statisticsByHubInfo);
}
