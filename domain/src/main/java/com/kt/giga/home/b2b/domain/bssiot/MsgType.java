package com.kt.giga.home.b2b.domain.bssiot;

/**
 * Created by alsdnfl on 2017-01-13.
 */
public enum MsgType {

    BIZCONTINFOREQ("0150","Biz계약정보 전송"),
    BIZCONTINFORES("0151","Biz계약정보 전송 Ack"),
    PREOPENREQ("0200","선개통 결과 전송"),
    PREOPENRES("0201","선개통 결과 전송 Ack"),
    CONTSTTUSCHGRSTREQ("1750","계약상태변경 결과 전송"),
    CONTSTTUSCHGRSTRES("1751","계약상태변경 결과 전송 Ack"),
    SVCACTVAREQ("1200","Service Activation"),
    SVCACTVARES("1201","Service Activation Ack"),
    CONTSTTUSCHGREQ("1500","서비스상태 변경"),
    CONTSTTUSCHGRES("1501","서비스상태 변경 Ack"),
    USEQNTREQ("6050","사용량 제공"),
    USEQNTRES("6051","사용량 제공 Ack");

    private String code;
    private String msg;

    MsgType(String code, String msg ) {
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
