package com.kt.giga.home.b2b.domain.bssiot.receiver;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;

/**
 * Created by 민우 on 2017-01-08.
 */

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "", propOrder = {"commonHeader", "contSttusChgRstReqInfo"})
@XmlRootElement(name = "ContSttusChgRstRequest")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class ContSttusChgRstRequest extends BssIotBase {

    @Valid
    private ContSttusChgRstReqInfo contSttusChgRstReqInfo;

    @XmlElement(name = "contSttusChgRstReqInfo", namespace = "http://www.kt.com/m2m/domain/svcResProvider", required = true)
    public ContSttusChgRstReqInfo getContSttusChgRstReqInfo() {
        return contSttusChgRstReqInfo;
    }

    public void setContSttusChgRstReqInfo(ContSttusChgRstReqInfo contSttusChgRstReqInfo) {
        this.contSttusChgRstReqInfo = contSttusChgRstReqInfo;
    }
}
