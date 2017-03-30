package com.kt.giga.home.b2b.domain;

import com.kt.giga.home.b2b.annotation.ExcelFieldName;
import lombok.Data;

import java.util.List;

/**
 * Created by junypooh on 2017-03-17.
 * <pre>
 * com.kt.giga.home.b2b.domain.StatisticsByHubInfo
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-17 오후 5:07
 */
@Data
public class StatisticsByHubInfo {

    private String bizContSerial;

    @ExcelFieldName(name="서비스 계약번호" ,order=1)
    private Long serialNumber;

    @ExcelFieldName(name="상세주소" ,order=2)
    private String devicePlace;

    @ExcelFieldName(name="이름" ,order=3)
    private String userName;

    private Double avgUsage;

    private char avgUsageUnitCd;

    private Double usage;

    private char usageUnitCd;

    @ExcelFieldName(name="평균사용량(MB)" ,order=4)
    private Long viewAvgUsage;

    @ExcelFieldName(name="전월사용량(MB)" ,order=5)
    private Long viewUsage;

    private List<Long> serialNumbers;
}
