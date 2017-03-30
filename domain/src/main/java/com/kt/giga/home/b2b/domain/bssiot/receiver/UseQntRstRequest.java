package com.kt.giga.home.b2b.domain.bssiot.receiver;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;

/**
 * Created by 민우 on 2017-01-08.
 */

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "", propOrder = {"commonHeader", "useQntRstReqInfo"})
@XmlRootElement(name = "UseQntRstRequest")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class UseQntRstRequest extends BssIotBase {

    @Valid
    protected UseQntRstReqInfo useQntRstReqInfo;

    @XmlElement(name = "useQntRstReqInfo", namespace = "http://www.kt.com/m2m/domain/svcResProvider", required = true)
    public UseQntRstReqInfo getUseQntRstReqInfo() {
        return useQntRstReqInfo;
    }

    public void setUseQntRstReqInfo(UseQntRstReqInfo useQntRstReqInfo) {
        this.useQntRstReqInfo = useQntRstReqInfo;
    }
}
