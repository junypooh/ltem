package com.kt.giga.home.b2b.entity;

import com.kt.giga.home.b2b.converters.Aes256CryptoConverter;
import com.kt.giga.home.b2b.domain.DeviceStatus;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by cecil on 2017. 1. 14..
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false, of = {"serialNumber"})
@Entity
@ToString(exclude = {"currentBizContract", "deviceModel", "historicBizContracts"}, callSuper = true)
@Table(name = "svc_contract", schema = "ltem")
public class ServiceContract extends BaseCreatedByModifiedByEntity implements Serializable {

    private static final long serialVersionUID = 5107011497635529180L;

    @Id
    @NotNull
    @Column(name = "svc_cont_serial", updatable = false)
    private Long serialNumber;

    @Convert(converter = Aes256CryptoConverter.class)
    @Column(name = "iccid", updatable = false, nullable = false)
    private String iccId;

    @Convert(converter = Aes256CryptoConverter.class)
    @Column(name = "imei", updatable = false, nullable = false)
    private String imei;

    @Column(name = "device_status_cd", nullable = false)
    private Short deviceStatus;

    @Convert(converter = Aes256CryptoConverter.class)
    @Column(name = "ctn")
    private String ctn;

    @Convert(converter = Aes256CryptoConverter.class)
    @Column(name = "user_ctn")
    private String userCtn;

    @Convert(converter = Aes256CryptoConverter.class)
    @Column(name = "auth_number")
    private String authNumber;

    @Convert(converter = Aes256CryptoConverter.class)
    @Column(name = "secret_key")
    private String secretKey;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "biz_cont_serial", nullable = false, referencedColumnName = "biz_cont_serial")
    private BizContract currentBizContract;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "device_model_cd", nullable = false, referencedColumnName = "device_model_cd")
    private DeviceModel deviceModel;

    @Column(name = "is_active")
    private Boolean isActive = false;

    @Column(name = "activated")
    private LocalDateTime activated;

    @Column(name = "is_suspended")
    private Boolean isSuspended = false;

    @Column(name = "is_mapped")
    private Boolean isUserMapped = false;

    @Column(name = "device_place")
    private String devicePlace;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "is_certify", insertable = false)
    private Boolean isCertify;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "serviceContract")
    private Set<SvcContUsage> svcusages;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "historicServiceContracts")
    private Set<BizContract> historicBizContracts;

    public void setDeviceStatus(DeviceStatus deviceStatus) {
        this.deviceStatus = deviceStatus.getStatusCd();
    }

    public DeviceStatus getDeviceStatus() {
        return DeviceStatus.getStatus(this.deviceStatus);
    }

    public short getDeviceStatusCd() {
        return this.deviceStatus;
    }

    @Column(name = "svc_tgt_seq")
    private Long svcTgtSeq;

    @Column(name = "spot_dev_seq")
    private Long spotDevSeq;

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
            @JoinColumn(name = "spot_dev_seq", referencedColumnName = "spot_dev_seq", insertable = false, updatable = false),
            @JoinColumn(name = "svc_tgt_seq", referencedColumnName = "svc_tgt_seq", insertable = false, updatable = false)
    })
    public SpotDevBas spotDevBas;
}
