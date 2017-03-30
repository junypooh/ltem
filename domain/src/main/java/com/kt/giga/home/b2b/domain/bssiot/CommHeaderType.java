package com.kt.giga.home.b2b.domain.bssiot;

/**
 * Created by alsdnfl on 2017-01-13.
 */
public enum CommHeaderType {
    CMPN_CD_HOM("ESTHOM", "사업자코드"),
    CMPN_CD_KT("KT", "사업자코드"),
    USER_ID("hm1234", "사용자 ID"),
    CH_TYPE("W", "채널 유형");

    private String code;
    private String msg;

    CommHeaderType(String code, String msg ) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

}
