package com.kt.giga.home.b2b.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Set;

/**
 * Created by DaDa on 2017-02-06.
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = false, of = {"serialNumber"})
public class ContractInfo implements Serializable {

    private static final long serialVersionUID = -1459075309385377775L;

    public ContractInfo(){}

    public ContractInfo(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public ContractInfo(String serialNumber, String name) {
        this.serialNumber = serialNumber;
        this.name = name;
    }
    
    /** 계약번호 */
    private String serialNumber;

    /** 계약명 */
    private String name;

    /** 계약 회사 코드 */
    private String companyCd;

    /** 계약 시작 일시 */
    private LocalDate startDate;

    /** 계약 종료 일시 */
    private LocalDate endDate;

    /** 허용량 */
    private Long capacity;

    /** 허용량 단위코드 */
    private char capacityUnitCd;

    /** 허용량 MB로 계산 된 내용 */
    private Long viewCapacity;

    /** 계약 상태 */
    private String contractState;

    /** 계약 담당자들 */
    private Set<B2bManager> managers;

    /** 계약 담당자(Master) */
    private Set<B2bManager> master;

    /** 계약 담당자(Supervisor) */
    private Set<B2bManager> supervisor;

    /** 계약 담당자(Staff) */
    private Set<B2bManager> staff;

    /** 회선수 */
    private int circuitCount;

    /** 계약설명 */
    private String description;

    /**
     * 계약 상태
     * @return
     */
    public String getContractState() {

        Period startBetween = Period.between(getStartDate(), LocalDateTime.now().toLocalDate());
        Period endBetween = Period.between(LocalDateTime.now().toLocalDate(), getEndDate());

        if(startBetween.getDays() < 0)
            return "계약";
        else if(startBetween.getDays() >= 0 && endBetween.getDays() >= 0)
            return "계약중";
        else
            return "계약만료";
    }

    private List<Long> svcContSerials;

    /**
     * 계약 시작 일시 String 변환
     * @return
     */
    public String getStartDateToString() {
        return this.startDate.toString();
    }

    /**
     * 계약 종료 일시 String 변환
     * @return
     */
    public String getEndDateToString() {
        return this.endDate.toString();
    }

    /** 서비스 장치 주소 미등록건 */
    private Long devicePlaceIsNullCount;

    /** 서비스 장치 count */
    private int devicePlaceCount;

    /** 허브 연결 수 **/
    private long hubConnectCount;

    /** 최근(지난 달) 사용 량 */
    private ContractUsageInfo recentContUsage;



}
