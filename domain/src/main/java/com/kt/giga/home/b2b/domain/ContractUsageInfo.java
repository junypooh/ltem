package com.kt.giga.home.b2b.domain;

import com.kt.giga.home.b2b.annotation.ExcelFieldName;
import lombok.Data;

import java.util.List;

/**
 * Created by junypooh on 2017-03-15.
 * <pre>
 * com.kt.giga.home.b2b.domain.ContractUsageInfo
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-15 오후 5:54
 */
@Data
public class ContractUsageInfo {

    private String serialNumber;

    private short year;

    @ExcelFieldName(name="월" ,order=1)
    private short month;

    private Long capacity;

    private Long usage;

    private char capacityUnit;

    public void setCapacityUnit(CapacityUnit capacityUnit) {
        this.capacityUnit = capacityUnit.getUnit();
    }

    public CapacityUnit getCapacityUnit() {
        return CapacityUnit.getCapacityUnit(this.capacityUnit);
    }

    public char getCapacityUnitCd() {
        return this.capacityUnit;
    }

    /** 허용량 MB로 계산 된 내용 */
    @ExcelFieldName(name="허용량(MB)" ,order=2)
    private Long viewCapacity;

    @ExcelFieldName(name="사용중인 회선수" ,order=3)
    private int activatedCount;

    @ExcelFieldName(name="허브 사용자" ,order=4)
    private int mappedCount;

    private List<Short> months;
}
