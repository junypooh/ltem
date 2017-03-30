package com.kt.giga.home.b2b.domain.bssiot;

/**
 * Created by 민우 on 2017-01-12.
 */
public enum ResponseType {

    SUCCESS("S00000", "성공"),
    INVALID_LENGTH("F10001", "항목 길이 오류"),
    INVALID_FORM("F10002", "항목 형식 오류"),
    MISSING_VALUE("F10003", "요청전문 내 필수값 누락"),
    UNDEFINED_VALUE("F10004", "정의되지 않은 요청값"),
    UNREGISTERED_CARRIER_CODE("F10005", "미등록된 사업자코드"),
    UNREGISTERED_SERVICE("F20001", "미등록된 서비스계약번호"),
    UNREGISTERED_ACCOUNT("F20002", "미등록된 청구계정번호"),
    UNREGISTERED_CUSTOMER("F20003", "미등록된 고객번호"),
    ETC_DATAERROR("F29999", "기타 오류 (BSS-IoT 내 데이터 불일치)"),
    ETC_KT_INSIDE("F99998", "기타 오류 (KT 내부 연동 오류)"),
    ETC_ERROR("F99999", "내부시스템 오류 (BSS-IoT 내부 시스템 오류)"),
    INVALID_TOTALCNT("F20005", "전체 건수 불일치"),
    UNREGISTERED_USIM("F20006", "미등록된 USIM"),
    UNREGISTERED_DEVICE("F20007", "미등록된 단말기"),
    USING_USIM("F20008", "사용중인 USIM"),
    USING_DEVICE("F20009", "사용중인 단말기"),
    FAIL_USIM("F20010", "기타 개통 불가 USIM"),
    FAIL_DEVICE("F20011", "기타 개통 불가 단말기"),
    NONE_SERVICE("F20012", "유효한 서비스계약번호 없음{상세내용}"),
    CANNOT_REQ_PROCEEDING("F20013", "진행 중 상태에서 추가 요청 불가"),
    ACTION_TIME_OUT("F20014", "해지 복구 가능 기간 초과"),
    ENCRYPTION_ERROR("F20015", "암복호화 오류"),
    OVERLAP("F20023", "변경 요청한 사항이 기존 것과 동일"),
    HOME_ERROR("F99997", "홈매니저 내부 시스템 오류"),
    NOT_BIZCD("F20101", "존재하지 않는 BIZ_CONT_NO"),
    EXISTENT_TGTCD("F20102", "기 존재하는 BIZ_CONT_NO"),
    EQUAL_CD("F20103", "TGT_ BIZ_CONT_NO = BIZ_CONT_NO"),
    ETC("F99997", "홈매니저 내부 시스템 오류");


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
