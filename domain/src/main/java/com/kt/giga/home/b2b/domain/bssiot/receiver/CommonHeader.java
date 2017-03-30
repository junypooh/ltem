package com.kt.giga.home.b2b.domain.bssiot.receiver;

import com.kt.giga.home.b2b.domain.bssiot.CommHeaderType;
import com.kt.giga.home.b2b.domain.bssiot.MsgType;
import com.kt.giga.home.b2b.domain.bssiot.ResponseType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.*;

/**
 * Created by 민우 on 2016-12-08.
 */

/**
 * 항     목	항목명     	유형	길이	HOM	KT	비고
 * CMPN_CD	    사업자코드	Char	10	    M	M	- (응답전문포함)전문 송신 회사 코드
 * - 홈매니저에서 송신 시 : ESTHOM
 * - KT에서 송신 시 : KT
 * MSG_ID	    전문 ID	    Char	17	    M	M	- 요청전문을 보내는 측에서 생성함.
 * - 응답전문은 요청전문의 값을 그대로 사용함.
 * - 전송일자 + 전송시각 + 일련번호세자리로 구성되며 메시지별 unique한 식별값
 * ex)20150612152327001
 * MSG_TYPE	전문유형	Char	4	    M	M	2.2 전문 유형 정의 참조
 * USER_ID 	사용자 ID	Char	20	    M	M	KT에서 부여한 ID (상수)
 * - 홈매니저 : hm1234
 * CH_TYPE	    채널 유형	Char	1	    M	M	해당 전문의 인입 채널 구분자 (상수)
 * - W
 * RESULT_CODE	결과코드	Char	6		M	    3. 코드 정의 참조
 * RESULT_MSG	결과메시지	Char	100		O	    RESULT_CODE에 대한 메시지
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "commonHeader", propOrder = {
        "cmpnCd",
        "msgId",
        "msgType",
        "userId",
        "chType",
        "resultCode",
        "resultMsg"
})
@XmlRootElement(name = "commonHeader")
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class CommonHeader {

    public CommonHeader() {
    }

    public CommonHeader(CommHeaderType headerType, String msgId, MsgType msgType, String userId, String chType, ResponseType responseType) {
        this.cmpnCd = headerType.getCode();
        this.msgId = msgId;
        this.msgType = msgType.getCode();
        this.userId = userId;
        this.chType = chType;
        this.resultCode = responseType.getCode();
        this.resultMsg = responseType.getMsg();
    }

    @Pattern(regexp = "KT",message = "value")
    @NotNull(message = "empty")
    @NotEmpty(message = "empty")
    @Length(min = 1, max = 10, message = "length")
    @XmlElement(name = "CMPN_CD", namespace = "http://www.kt.com/m2m/commonHeader", required = true)
    protected String cmpnCd;

    @NotNull(message = "empty")
    @NotEmpty(message = "empty")
    @Length(min = 1, max = 17, message = "length")
    @XmlElement(name = "MSG_ID", namespace = "http://www.kt.com/m2m/commonHeader", required = true)
    protected String msgId;

    @Pattern(regexp = "0150|0200|1750|6050",message = "value")
    @NotNull(message = "empty")
    @NotEmpty(message = "empty")
    @Length(min = 1, max = 4, message = "length")
    @XmlElement(name = "MSG_TYPE", namespace = "http://www.kt.com/m2m/commonHeader", required = true)
    protected String msgType;

    @Pattern(regexp = "hm1234",message = "value")
    @NotNull(message = "empty")
    @NotEmpty(message = "empty")
    @Length(min = 1, max = 20, message = "length")
    @XmlElement(name = "USER_ID", namespace = "http://www.kt.com/m2m/commonHeader", required = true)
    protected String userId;

    @Pattern(regexp = "W",message = "value")
    @NotNull(message = "empty")
    @NotEmpty(message = "empty")
    @Length(min = 1, max = 1, message = "length")
    @XmlElement(name = "CH_TYPE", namespace = "http://www.kt.com/m2m/commonHeader", required = true)
    protected String chType;

    @XmlElement(name = "RESULT_CODE", namespace = "http://www.kt.com/m2m/commonHeader")
    protected String resultCode;

    @XmlElement(name = "RESULT_MSG", namespace = "http://www.kt.com/m2m/commonHeader")
    protected String resultMsg;

    public void resultSet(ResponseType responseType , MsgType msgType, CommHeaderType commHeaderType){
       setResultCode(responseType.getCode());
       setResultMsg(responseType.getMsg());
       setMsgType(msgType.getCode());
       setCmpnCd(commHeaderType.getCode());
    }
}
