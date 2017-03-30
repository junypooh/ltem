package com.kt.giga.home.b2b.core.dms.domain;

/**
 * Created by 민우 on 2016-12-02.
 */
public enum ResponseType {
    SUCCESS("OK", "성공"),
    FAIL("NOK", "실패");

    private String code;
    private String msg;

    ResponseType(String code, String msg ) {
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
