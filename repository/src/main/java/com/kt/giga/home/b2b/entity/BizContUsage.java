package com.kt.giga.home.b2b.entity;

import com.kt.giga.home.b2b.domain.CapacityUnit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by cecil on 2017. 1. 14..
 */
@Data
@IdClass(BizContUsage.BizContUsagePK.class)
@Entity
@ToString(exclude = {"bizContract"})
@EqualsAndHashCode(of = {"serialNumber", "year", "month"})
@Table(name = "biz_cont_usage", schema = "ltem")
public class BizContUsage implements Serializable {

    private static final long serialVersionUID = 8506308487252748849L;

    @Id
    @NotNull
    @Column(name = "biz_cont_serial", updatable = false)
    private String serialNumber;

    @Id
    @Column(name = "year", updatable = false)
    private short year;

    @Id
    @Column(name = "month", updatable = false)
    private short month;

    @Column(name = "capacity", updatable = false)
    private Long capacity;

    @Column(name = "usage", updatable = false)
    private Long usage;

    @Column(name = "capacity_unit_cd", updatable = false)
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

    @Column(name = "activated_count", updatable = false)
    private int activatedCount;

    @Column(name = "mapped_count", updatable = false)
    private int mappedCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "biz_cont_serial", referencedColumnName = "biz_cont_serial", insertable = false, updatable = false)
    private BizContract bizContract;

    public static long getCalculateCapacityByMb(char capacityUnit, Long capacity) {

        if (CapacityUnit.getCapacityUnit(capacityUnit).equals(CapacityUnit.MEGABYTE)) {
            return capacity;
        }

        int bytes = CapacityUnit.getCapacityUnit(capacityUnit).getBytes();
        long totalBytes = capacity * bytes;

        return totalBytes / 1024;
    }

    @Data
    public static class BizContUsagePK implements Serializable {

        private static final long serialVersionUID = -303259679685999202L;

        private String serialNumber;
        private short  year;
        private short  month;

        public BizContUsagePK() {

        }

        public BizContUsagePK(String serialNumber, short year, short month) {
            this.serialNumber = serialNumber;
            this.year = year;
            this.month = month;
        }

    }
}
