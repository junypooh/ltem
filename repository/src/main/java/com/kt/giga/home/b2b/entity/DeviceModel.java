package com.kt.giga.home.b2b.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by cecil on 2017. 1. 14..
 */
@Data
@Entity
@ToString(exclude = {"serviceContracts"})
@EqualsAndHashCode(of = {"code"})
@Table(name = "device_model", schema = "ltem")
public class DeviceModel implements Serializable {

    private static final long serialVersionUID = -4388593256357058798L;

    @Id
    @NotNull
    @Column(name = "device_model_cd", updatable = false)
    private String code;

    @Column(name = "device_model_name", updatable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "deviceModel")
    private Set<ServiceContract> serviceContracts;
}
