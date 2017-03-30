package com.kt.giga.home.b2b.domain;
import com.kt.giga.home.b2b.annotation.ExcelFieldName;
import lombok.Data;

/**
 * Created by junypooh on 2017-02-07.
 * <pre>
 * com.kt.giga.home.b2b.domain.SvcContractInfo
 *
 * 서비스 계약
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-02-07 오후 3:17
 */
@Data
public class SvcContractInfo {

    /**
     * 서비스 계약 번호
     */
    @ExcelFieldName(name="서비스계약번호" ,order=1)
    private Long svcContSerial;

    /**
     * 모계약 번호
     */
    private String bizContSerial;

    /**
     * USIM 일련번호 (AES256)
     */
    private String iccId;

    /**
     * 단말 일련번호 (AES256)
     */
    @ExcelFieldName(name="단말번호" ,order=2)
    private String imei;

    /**
     * 상세주소
     */
    @ExcelFieldName(name="상세주소" ,order=3)
    private String devicePlace;

    /**
     * 사용자명
     */
    @ExcelFieldName(name="사용자명" ,order=4)
    private String userName;

    /**
     * 전화번호
     */
    private String ctn;

    /**
     * 전화번호
     */
    @ExcelFieldName(name="전화번호" ,order=5)
    private String userCtn;

    /**
     * 인증번호
     */
    private String secretKey;

    /**
     * 서비스 활성화 여부
     */
    private Boolean isActive;

    /**
     * 사용자 연결 여부
     */
    private Boolean isUserMapped;
    /**
     * 정지 여부
     */
    private Boolean isSuspended;
    /**
     * 인증유무
     */
    @ExcelFieldName(name="인증유무" ,order=6, booleanValue = "인증완료/미인증")
    private Boolean isCertify;

    /**
     * 계약정보
     */
    private ContractInfo currentBizContract;

    /**
     * 장비 상태 코드
     */
    private short deviceStatusCd;

    /**
     * 화면에서 넘어오는 상태(사용/일시정지)
     */
    private String status;

    /**
     * 모계약 번호
     * @return
     */
    public String getBizContSerial() {
        if(getCurrentBizContract() != null) {
            return getCurrentBizContract().getSerialNumber();
        } else {
            return this.bizContSerial;
        }
    }

    private String authenticationNumber;

    private Long svcTgtSeq;

    private DeviceModelInfo deviceModel;


}
