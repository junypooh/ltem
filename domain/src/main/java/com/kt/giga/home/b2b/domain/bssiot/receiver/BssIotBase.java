package com.kt.giga.home.b2b.domain.bssiot.receiver;

import lombok.ToString;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * com.kt.giga.home.b2b.core.contract.domain
 * <p>
 * Created by cecil on 2017. 1. 22..
 */
@ToString
@XmlTransient
public class BssIotBase {

    public BssIotBase() {
    }

    public BssIotBase(CommonHeader commonHeader) {
        this.commonHeader = commonHeader;
    }

    @Valid
    protected CommonHeader commonHeader;

    @XmlElement(namespace = "http://www.kt.com/m2m/domain/svcResProvider", required = true)
    public CommonHeader getCommonHeader() {
        return commonHeader;
    }

    public void setCommonHeader(CommonHeader commonHeader) {
        this.commonHeader = commonHeader;
    }
}
