package com.kt.giga.home.b2b.entity;

import com.kt.giga.home.b2b.domain.CapacityUnit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by mac on 2017. 1. 16..
 */
@IdClass(SvcContUsage.SvcContUsagePK.class)
@Entity
@Data
@EqualsAndHashCode(of = {"serialNumber", "year", "month"})
@ToString(exclude = "serviceContract")
@Table(name = "svc_cont_usage", schema = "ltem")
public class SvcContUsage {

    @Id
    @NotNull
    @Column(name = "svc_cont_serial", updatable = false)
    private Long serialNumber;

    @Id
    @NotNull
    @Column(name = "year", updatable = false)
    private short year;

    @Id
    @NotNull
    @Column(name = "month", updatable = false)
    private short month;

    @NotNull
    @Column(name = "usage", updatable = false)
    private Long usage;

    @NotNull
    @Column(name = "usage_unit_cd", updatable = false)
    private char usageUnitCd;

    public void setUsageUnitCd(CapacityUnit capacityUnit) {
        this.usageUnitCd = capacityUnit.getUnit();
    }

    public CapacityUnit getCapacityUnit() {
        return CapacityUnit.getCapacityUnit(this.usageUnitCd);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "svc_cont_serial", referencedColumnName = "svc_cont_serial", insertable = false, updatable = false)
    private ServiceContract serviceContract;

    @Data
    public static class SvcContUsagePK implements Serializable {

        private static final long serialVersionUID = 2373905388924693260L;

        private Long  serialNumber;
        private short year;
        private short month;

        public SvcContUsagePK() {
        }

        public SvcContUsagePK(long serialNumber, short year, short month) {
            this.serialNumber = serialNumber;
            this.year = year;
            this.month = month;
        }

    }
}










