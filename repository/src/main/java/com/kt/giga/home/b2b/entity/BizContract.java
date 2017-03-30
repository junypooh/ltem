package com.kt.giga.home.b2b.entity;

import com.kt.giga.home.b2b.domain.CapacityUnit;
import com.kt.giga.home.b2b.domain.PeriodUnit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * Created by cecil on 2017. 1. 14..
 */
@Data
@EqualsAndHashCode(callSuper = false, of = {"serialNumber"})
@Entity
@ToString(exclude = {"company", "currentServiceContracts", "managers", "previousBizContracts", "nextBizContracts", "usages", "historicServiceContracts"}, callSuper = true)
@Table(name = "biz_contract", schema = "ltem")
public class BizContract extends BaseCreatedByModifiedByEntity implements Serializable {

    private static final long serialVersionUID = -8009551985463966835L;

    @Id
    @NotNull
    @Column(name = "biz_cont_serial", updatable = false)
    private String serialNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_cd", nullable = false, referencedColumnName = "company_cd")
    private Company company;

    @Column(name = "business_cd", nullable = false)
    private String businessCd;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "period_cd", nullable = false)
    private char periodUnit;

    public void setPeriodUnit(PeriodUnit periodUnit) {
        this.periodUnit = periodUnit.getUnit();
    }

    public PeriodUnit getPeriodUnit() {
        return PeriodUnit.getPeriodUnit(Character.toUpperCase(this.periodUnit));
    }

    @Column(name = "period", nullable = false)
    private Integer period;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "capacity_unit_cd", nullable = false)
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

    @Column(name = "capacity", nullable = false)
    private Long capacity;

    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "currentBizContract")
    private Set<ServiceContract> currentServiceContracts;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "bizContractRel",
            schema = "ltem",
            joinColumns = @JoinColumn(name = "curr_biz_cont_serial"),
            inverseJoinColumns = @JoinColumn(name = "prev_biz_cont_serial")
    )
    private Set<BizContract> previousBizContracts;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "previousBizContracts")
    private Set<BizContract> nextBizContracts;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "bizContracts")
    private Set<Manager> managers;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "bizContract")
    private Set<BizContUsage> usages;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "biz_cont_svc_cont_rel",
            schema = "ltem",
            joinColumns = @JoinColumn(name = "biz_cont_serial"),
            inverseJoinColumns = @JoinColumn(name = "svc_cont_serial")
    )
    private Set<ServiceContract> historicServiceContracts;

    public static Integer getCircuitCount(Set<ServiceContract> currentServiceContracts){
        return currentServiceContracts.size();
    }

    public static Long getDevicePlaceIsNullCount(Set<ServiceContract> currentServiceContracts) {

        return currentServiceContracts.parallelStream()
                                      .filter(serviceContract -> !StringUtils.isNotBlank(serviceContract.getDevicePlace()))
                                      .count();
    }

    public static int getDevicePlaceCount(Set<ServiceContract> currentServiceContracts) {
        return currentServiceContracts.size();
    }

    public static long getCalculateCapacityByMb(char capacityUnit, Long capacity) {

        if (CapacityUnit.getCapacityUnit(capacityUnit).equals(CapacityUnit.MEGABYTE)) {
            return capacity;
        }

        int bytes = CapacityUnit.getCapacityUnit(capacityUnit).getBytes();
        long totalBytes = capacity * bytes;

        return totalBytes / 1024;
    }

    public static long getHubConnectCount(Set<ServiceContract> currentServiceContracts) {

        return currentServiceContracts.parallelStream()
                                      .map(ServiceContract::getSpotDevBas)
                                      .filter(spotDevBas -> spotDevBas != null && "1".equals(spotDevBas.getRemark()))
                                      .count();
    }


}
