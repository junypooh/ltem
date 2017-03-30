package com.kt.giga.home.b2b.domain.bssiot.receiver;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;

/**
 * Created by 민우 on 2017-01-06.
 */

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "", propOrder = {"commonHeader", "bssIotBizContractDto"})
@XmlRootElement(name = "BizContInfoRstRequest")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class BizContractRequest extends BssIotBase {

    @Valid
    private BssIotBizContractDto bssIotBizContractDto;

    @XmlElement(name = "bizContInfoRstReqInfo", namespace = "http://www.kt.com/m2m/domain/svcResProvider", required = true)
    public BssIotBizContractDto getBssIotBizContractDto() {
        return bssIotBizContractDto;
    }

    public void setBssIotBizContractDto(BssIotBizContractDto bssIotBizContractDto) {
        this.bssIotBizContractDto = bssIotBizContractDto;
    }
}
