package com.kt.giga.home.b2b.domain.bssiot.receiver;

/**
 * Created by 민우 on 2017-01-08.
 */

import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@ToString(callSuper = true)
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "", propOrder = {"commonHeader"})
@XmlRootElement(name = "ContSttusChgRstResponse")
public class ContSttusChgRstResponse extends BssIotBase {

    public ContSttusChgRstResponse() {
    }

    public ContSttusChgRstResponse(CommonHeader commonHeader) {
        super(commonHeader);
    }
}
