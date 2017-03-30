package com.kt.giga.home.b2b.domain.bssiot.receiver;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;

/**
 * Created by 민우 on 2017-01-08.
 */

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "", propOrder = {"commonHeader", "preOpenRstReqInfo"})
@XmlRootElement(name = "PreOpenRstRequest")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class PreOpenRstRequest extends BssIotBase {

    @Valid
    protected PreOpenRstReqInfo preOpenRstReqInfo;

    @XmlElement(name = "preOpenRstReqInfo", namespace = "http://www.kt.com/m2m/domain/svcResProvider", required = true)
    public PreOpenRstReqInfo getPreOpenRstReqInfo() {
        return preOpenRstReqInfo;
    }

    public void setPreOpenRstReqInfo(PreOpenRstReqInfo preOpenRstReqInfo) {
        this.preOpenRstReqInfo = preOpenRstReqInfo;
    }
}
