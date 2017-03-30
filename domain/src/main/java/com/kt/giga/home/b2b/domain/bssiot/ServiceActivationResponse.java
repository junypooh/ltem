package com.kt.giga.home.b2b.domain.bssiot;

import lombok.Data;

/**
 * Created by alsdnfl on 2017-03-14.
 */
@Data
public class ServiceActivationResponse {

    protected String resultCode;
    protected String resultMsg;

    public ServiceActivationResponse(){
        super();
    }

    public ServiceActivationResponse(String resultCode, String resultMsg) {
        super();
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public ServiceActivationResponse(ResponseType responseType) {
        this(responseType.getCode(), responseType.getMsg());
    }

}
