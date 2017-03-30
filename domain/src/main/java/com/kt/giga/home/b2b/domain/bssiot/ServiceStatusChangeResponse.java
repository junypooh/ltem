package com.kt.giga.home.b2b.domain.bssiot;

import lombok.Data;

/**
 * Created by alsdnfl on 2017-03-14.
 */
@Data
public class ServiceStatusChangeResponse {
    protected String resultCode;
    protected String resultMsg;

    public ServiceStatusChangeResponse(){
        super();
    }

    public ServiceStatusChangeResponse(String resultCode, String resultMsg) {
        super();
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public ServiceStatusChangeResponse(ResponseType responseType) {
        this(responseType.getCode(), responseType.getMsg());
    }
}
