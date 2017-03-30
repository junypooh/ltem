package com.kt.giga.home.b2b.core.dms.domain;

import lombok.Data;

/**
 * Created by 민우 on 2016-11-24.
 */
@Data
public class DeviceResponse {
    protected String resultCode;
    protected String resultMsg;

    public DeviceResponse(){
        super();
    }

    public DeviceResponse(String resultCode, String resultMsg) {
        super();
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }


}
