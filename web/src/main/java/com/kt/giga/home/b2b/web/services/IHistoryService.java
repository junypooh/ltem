package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.HistoryInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by DaDa on 2017-03-22.
 */
public interface IHistoryService {

    /**
     * 이력조회
     * @param page
     * @return
     */
    Map<String, Object> getHistoryInfo(int page);

    /**
     * 회사별 이력 조회
     * @param companyCd
     * @param startDt
     * @param endDt
     * @param page
     * @return
     */
    Map<String, Object> getHistoryInfoGroupByCompany(String companyCd, String startDt, String endDt, int page);

    /**
     * 회사별 이력 조회 > 마스킹 해제
     * @param companyCd
     * @param managerUserId
     * @param startDt
     * @param endDt
     * @param page
     * @return
     */
    Map<String, Object> getHistoryInfoGroupByUnmaskCompany(String companyCd, String managerUserId, String startDt, String endDt, int page);

    /**
     * 이력조회 > 엑셀 다운로드
     * @param historyInfo
     * @return
     */
    List<HistoryInfo> getHistoryToExcel(HistoryInfo historyInfo);

    /**
     * 담당자별 이력 조회
     * @param managerUserId
     * @param startDt
     * @param endDt
     * @param page
     * @return
     */
    Map<String, Object> getHistoryInfoGroupByManager(String managerUserId, String startDt, String endDt, int page);


    /**
     * 담당자별 이력 조회 > 엑셀 다운로드
     * @param historyInfo
     * @return
     */
    List<HistoryInfo> getHistoryGroupByManagerToExcel(HistoryInfo historyInfo);

    /**
     * 담당자별 이력 조회 > 마스킹 해제
     * @param historySeq
     * @return
     */
    HistoryInfo getHistoryInfoGroupByUnmaskManager(Integer historySeq);


    /**
     * 이력조회 > 회사별 이력 조회 > 엑셀 다운로드
     * @param historyInfo
     * @param companyCd
     * @param startDt
     * @param endDt
     * @return
     */
    List<HistoryInfo> getCompanyHistoryToExcel(HistoryInfo historyInfo, String companyCd, String startDt, String endDt);
}
