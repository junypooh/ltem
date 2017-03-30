package com.kt.giga.home.b2b.domain;

import com.kt.giga.home.b2b.annotation.ExcelFieldName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by junypooh on 2017-03-22.
 * <pre>
 * com.kt.giga.home.b2b.domain.HistoryInfo
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-22 오후 5:57
 */
@Data
public class HistoryInfo {

    private Integer historySeq;

    private String companyCd;

    @ExcelFieldName(name="기업명")
    private String companyName;

    @ExcelFieldName(name="아이디")
    private String managerMaskUserId;

    private String managerUserId;

    private String managerName;

    @ExcelFieldName(name="이름")
    private String managerMaskName;

    @ExcelFieldName(name="권한")
    private String authorityName;

    @ExcelFieldName(name="마스킹해제 수")
    private Integer maskedCount;

    @ExcelFieldName(name="다운로드 수")
    private Integer downloadCount;

    @ExcelFieldName(name="계약번호")
    private String bizContSerial;

    @ExcelFieldName(name="위치")
    private String menuPath;

    @ExcelFieldName(name="이름")
    private String userName;

    private String userMaskName;

    @ExcelFieldName(name="정보")
    private String targetInfo;

    private String reason;

    @ExcelFieldName(name="최종이력 일시")
    private String lastHistoryDate;

    private List<String> companyCds;

    private List<String> managerUserIds;

    private String startDt;

    private String endDt;

    @ExcelFieldName(name="작업 내용")
    private String workContext;

    private List<Integer> historySeqs;

    @ExcelFieldName(name="작업 일시")
    private String workHistoryDate;

    private Boolean unmasked;

    private Boolean downloaded;

    private LocalDateTime audited;
}
