package com.kt.giga.home.b2b.domain.bssiot.sender;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;

/**
 * Created by alsdnfl on 2017-02-06.
 */
@Data
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {"commonHeader", "svcActvaReqInfo"})
@XmlRootElement(name = "SvcActvaRequest")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class SvcActvaRequest extends BssIotBase{

    @Valid
    private SvcActvaReqInfo svcActvaReqInfo;

    @XmlElement(name = "svcActvaReqInfo", required = true)
    public SvcActvaReqInfo getSvcActvaReqInfo() {return svcActvaReqInfo;}

    public void setSvcActvaReqInfo(SvcActvaReqInfo svcActvaReqInfo){
        this.svcActvaReqInfo = svcActvaReqInfo;
    }
}
