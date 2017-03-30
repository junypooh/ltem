package com.kt.giga.home.b2b.domain.bssiot.sender;

import lombok.ToString;

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

    protected CommonHeader commonHeader;

    @XmlElement(required = true)
    public CommonHeader getCommonHeader() {
        return commonHeader;
    }

    public void setCommonHeader(CommonHeader commonHeader) {
        this.commonHeader = commonHeader;
    }

}
