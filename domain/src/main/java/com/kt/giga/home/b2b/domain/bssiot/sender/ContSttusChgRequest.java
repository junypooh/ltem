package com.kt.giga.home.b2b.domain.bssiot.sender;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;

/**
 * Created by alsdnfl on 2017-02-06.
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "", propOrder = {"commonHeader", "contSttusChgReqInfo"})
@XmlRootElement(name = "ContSttusChgRequest")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class ContSttusChgRequest extends BssIotBase {

    @Valid
    private ContSttusChgReqInfo contSttusChgReqInfo;

    @XmlElement(name = "contSttusChgReqInfo", required = true)
    public ContSttusChgReqInfo getContSttusChgReqInfo() {
        return contSttusChgReqInfo;
    }

    public void setContSttusChgReqInfo(ContSttusChgReqInfo contSttusChgReqInfo) {
        this.contSttusChgReqInfo = contSttusChgReqInfo;
    }
}
