package com.kt.giga.home.b2b.repository;

import com.kt.giga.home.b2b.domain.HistoryInfo;
import com.kt.giga.home.b2b.entity.PrivacyAuditHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by junypooh on 2017-03-21.
 * <pre>
 * com.kt.giga.home.b2b.repository.PrivacyAuditHistoryRepository
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-21 오후 5:56
 */
public interface PrivacyAuditHistoryRepository extends JpaRepository<PrivacyAuditHistory, Integer> {

    /**
     * 이력조회 목록
     * @return
     */
    @Query(value =
            "       SELECT company_cd                                                                                                               " +
            "           , company_name                                                                                                              " +
            "           ,(SELECT count(company_cd) FROM ltem.privacy_audit_history WHERE is_unmasked = true GROUP BY company_cd) as maskCnt       " +
            "           ,(SELECT count(company_cd) FROM ltem.privacy_audit_history WHERE is_downloaded = true GROUP BY company_cd) as downCnt      " +
            "           , count(1) over() as tot_cnt                                                                                                " +
            "       FROM ltem.privacy_audit_history                                                                                                 " +
            "       GROUP BY company_cd, company_name                                                                                               " +
            "       limit :limit offset :offset                                                                                                     "
            , nativeQuery = true)
    List<Object[]> findByHistoryInfo(@Param("limit") int limit, @Param("offset") int offset);

    /**
     * 이력조회 목록 엑셀 다운로드
     * @return
     */
    @Query(value =
            "       SELECT company_cd                                                          " +
            "           , company_name                                                         " +
            "           , SUM(CASE WHEN is_unmasked = true THEN 1 ELSE 0 END) as maskCnt       " +
            "           , SUM(CASE WHEN is_downloaded = true THEN 1 ELSE 0 END) as downCnt     " +
            "           , count(1) over() as tot_cnt                                           " +
            "       FROM ltem.privacy_audit_history                                            " +
            "       WHERE company_cd = :companyCd                                              " +
            "       GROUP BY company_cd, company_name                                          "
            , nativeQuery = true)
    List<Object[]> findByHistoryInfoToExcel(@Param("companyCd") String companyCd);

    /**
     * 회사별 이력조회 목록
     * @return
     */
    @Query(value =
            "   SELECT                                                                                                       " +
            "           manager_user_id, manager_name, authority_name                                                        " +
            "           , SUM(CASE WHEN is_unmasked = true THEN 1 ELSE 0 END) as maskCount                                   " +
            "           , SUM(CASE WHEN is_downloaded = true THEN 1 ELSE 0 END) as downCount                                 " +
            "           , to_char(MAX(audited), 'yyyy-mm-dd hh24:mi:ss') as audited                                          " +
            "           , COUNT(1) over() as tot_cnt                                                                         " +
            "   FROM ltem.privacy_audit_history                                                                              " +
            "   WHERE company_cd = :companyCd                                                                                " +
            "   AND audited BETWEEN to_date(:startDt, 'yyyymm') AND to_date(:endDt, 'yyyymm') + INTERVAL '1 month'           " +
            "   GROUP BY manager_user_id, manager_name, authority_name                                                       " +
            "   limit :limit offset :offset                                                                                  "
            , nativeQuery = true)
    List<Object[]> findByHistoryInfoGroupByCompanyCd(
                                                    @Param("companyCd") String companyCd
                                                    , @Param("startDt") String startDt
                                                    , @Param("endDt") String endDt
                                                    , @Param("limit") int limit
                                                    , @Param("offset") int offset);

    /**
     * 회사별 이력조회 목록
     * @return
     */
    @Query(value =
                    "   SELECT                                                                                                       " +
                    "           manager_user_id, manager_name, authority_name                                                        " +
                    "           , SUM(CASE WHEN is_unmasked = true THEN 1 ELSE 0 END) as maskCount                                   " +
                    "           , SUM(CASE WHEN is_downloaded = true THEN 1 ELSE 0 END) as downCount                                 " +
                    "           , to_char(MAX(audited), 'yyyy-mm-dd hh24:mi:ss') as audited                                          " +
                    "           , COUNT(1) over() as tot_cnt                                                                         " +
                    "   FROM ltem.privacy_audit_history                                                                              " +
                    "   WHERE company_cd = :companyCd                                                                                " +
                    "   AND manager_user_id = :managerUserId                                                                                " +
                    "   AND audited BETWEEN to_date(:startDt, 'yyyymm') AND to_date(:endDt, 'yyyymm') + INTERVAL '1 month'           " +
                    "   GROUP BY manager_user_id, manager_name, authority_name                                                       "
            , nativeQuery = true)
    List<Object[]> findByHistoryInfoGroupByCompanyCdAndManagerUserId(
                                                    @Param("companyCd") String companyCd
                                                    , @Param("managerUserId") String managerUserId
                                                    , @Param("startDt") String startDt
                                                    , @Param("endDt") String endDt);

    /**
     * 회사별 이력조회 회사 정보
     * @return
     */
    @Query(value =
            "   SELECT                                                                                          " +
            "       SUM(CASE WHEN is_unmasked = true THEN 1 ELSE 0 END) as maskCount                            " +
            "       , SUM(CASE WHEN is_downloaded = true THEN 1 ELSE 0 END) as downCount                        " +
            "       , (SELECT company_name FROM ltem.company WHERE company_cd = :companyCd) as company_name     " +
            "   FROM ltem.privacy_audit_history                                                                      " +
            "   WHERE company_cd = :companyCd                                                                   " +
            "   AND audited BETWEEN to_date(:startDt, 'yyyymm') AND to_date(:endDt, 'yyyymm') + INTERVAL '1 month'           " +
            "   GROUP BY company_cd                                                                             "
            , nativeQuery = true)
    List<Object[]> findByHistoryInfoCompanyInfo(
                                                        @Param("companyCd") String companyCd
                                                        , @Param("startDt") String startDt
                                                        , @Param("endDt") String endDt);


    /**
     * 담당자별 이력조회
     * @param managerUserId
     * @param startDt
     * @param endDt
     * @param limit
     * @param offset
     * @return
     */
    @Query(value =
            "   SELECT history_seq                                                                                                     "+
            "        , CASE WHEN biz_cont_serial is null THEN '-' ELSE biz_cont_serial END as biz_cont_serial                         "+
            "        , menu_path                                                                                                       "+
            "        , CASE WHEN user_name is null THEN '' ELSE user_name END as user_name                                              "+
            "        , target_info                                                                                                     "+
            "        , CASE WHEN is_unmasked = true THEN '마스킹 해제'  WHEN is_downloaded = true THEN '다운로드' END as workContext      "+
            "        , CASE WHEN (is_unmasked = true and reason is not null) THEN '해제사유: ' || reason ELSE '' END as reason          "+
            "        , to_char(audited, 'yyyy-mm-dd hh24:mi:ss') as audited                                                            "+
            "        , COUNT(1) over() as tot_cnt                                                                                      " +
            "   FROM ltem.privacy_audit_history                                                                                        "+
            "   WHERE manager_user_id = :managerUserId                                                                                 "+
            "   AND audited BETWEEN to_date(:startDt, 'yyyymm') AND to_date(:endDt, 'yyyymm') + INTERVAL '1 month'                     "+
            "   ORDER BY audited DESC                                                                                                  "+
            "   limit :limit offset :offset                                                                                            "
            , nativeQuery = true)
    List<Object[]> findByHistoryInfoGroupByManager(@Param("managerUserId") String managerUserId
            , @Param("startDt") String startDt, @Param("endDt") String endDt, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * 담당자별 이력조회 Header(담당자 정보 조회)
     * @param managerUserId
     * @param startDt
     * @param endDt
     * @return
     */
    @Query(value =
            "   SELECT manager_user_id                                                                                      " +
            "        , authority_name                                                                                       " +
            "        , (                                                                                                    " +
            "   	    SELECT count(*)                                                                                     " +
            "   	    FROM ltem.privacy_audit_history                                                                     " +
            "   	    WHERE manager_user_id = :managerUserId                                                              " +
            "   	    AND audited BETWEEN to_date(:startDt, 'yyyymm') AND to_date(:endDt, 'yyyymm') + INTERVAL '1 month'  " +
            "   	    AND  is_downloaded = true                                                                           " +
            "        ) as downloadCnt                                                                                       " +
            "        , (                                                                                                    " +
            "   	    SELECT count(*)                                                                                     " +
            "   	    FROM ltem.privacy_audit_history                                                                     " +
            "   	    WHERE manager_user_id = :managerUserId                                                              " +
            "   	    AND audited BETWEEN to_date(:startDt, 'yyyymm') AND to_date(:endDt, 'yyyymm') + INTERVAL '1 month'  " +
            "   	    AND  is_unmasked = true                                                                             " +
            "        ) as maskedCnt                                                                                         " +
            "        , :startDt as strdate                                                                                  " +
            "        , :endDt as enddate                                                                                    " +
            "   FROM ltem.privacy_audit_history                                                                             " +
            "   WHERE manager_user_id = :managerUserId                                                                      " +
            "   GROUP BY manager_user_id, authority_name                                                                    "
            , nativeQuery = true)
    List<Object[]> findByHistoryInfoGroupByManagerHeader(@Param("managerUserId") String managerUserId, @Param("startDt") String startDt, @Param("endDt") String endDt);


    /**
     * 담당자별 이력조회 > 엑셀 다운로드
     * @param managerUserId
     * @param startDt
     * @param endDt
     * @return
     */
    @Query(value =
                "   SELECT history_seq                                                                                                     "+
                "        , CASE WHEN biz_cont_serial is null THEN '-' ELSE biz_cont_serial END as biz_cont_serial                         "+
                "        , menu_path                                                                                                       "+
                "        , CASE WHEN user_name is null THEN '' ELSE user_name END as user_name                                              "+
                "        , target_info                                                                                                     "+
                "        , CASE WHEN is_unmasked = true THEN '마스킹 해제'  WHEN is_downloaded = true THEN '다운로드' END as workContext      "+
                "        , CASE WHEN is_unmasked = true THEN '해제사유: ' || reason ELSE '' END as reason                                   "+
                "        , to_char(audited, 'yyyy-mm-dd hh24:mi:ss')                                                                       "+
                "   FROM ltem.privacy_audit_history                                                                                        "+
                "   WHERE manager_user_id = :managerUserId                                                                                 "+
                "   AND audited BETWEEN to_date(:startDt, 'yyyymm') AND to_date(:endDt, 'yyyymm') + INTERVAL '1 month'                     "+
                "   AND history_seq = :historySeq                                                                                          "+
                "   ORDER BY audited DESC                                                                                                  "
            , nativeQuery = true)
    List<Object[]> findByHistoryGroupByManagerToExcel(@Param("managerUserId") String managerUserId, @Param("startDt") String startDt, @Param("endDt") String endDt, @Param("historySeq") Integer historySeq);









}
