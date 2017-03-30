package com.kt.giga.home.b2b.service;

import com.kt.giga.home.b2b.domain.bssiot.*;
import com.kt.giga.home.b2b.domain.bssiot.sender.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by alsdnfl on 2017-03-13.
 */
@Slf4j
@Service
public class BssIotSenderService implements IBssIotSenderService {

    @Autowired
    IBssIotSenderWebService bssIotSenderWebService;

    @Override
    public ServiceActivationResponse serviceActivation(String svcContNo) {


        SvcActvaRequest svcActvaRequest = new SvcActvaRequest();

        CommonHeader commonHeader = new CommonHeader();
        SvcActvaReqInfo svcActvaReqInfo = new SvcActvaReqInfo();

        commonHeader.setCmpnCd(CommHeaderType.CMPN_CD_HOM.getCode());
        commonHeader.setMsgId(createMsgId());

        commonHeader.setMsgType(MsgType.SVCACTVAREQ.getCode());
        commonHeader.setUserId(CommHeaderType.USER_ID.getCode());
        commonHeader.setChType(CommHeaderType.CH_TYPE.getCode());
        commonHeader.setResultMsg("");
        commonHeader.setResultCode("");

        svcActvaReqInfo.setConttypecd("01");
        svcActvaReqInfo.setSvccontno(svcContNo);
        svcActvaReqInfo.setSetltypecd("");

        svcActvaRequest.setCommonHeader(commonHeader);
        svcActvaRequest.setSvcActvaReqInfo(svcActvaReqInfo);

        ServiceActivationResponse serviceActivationResponse;

        SvcActvaResponse svcActvaResponse = bssIotSenderWebService.svcActva(svcActvaRequest);

        serviceActivationResponse = new ServiceActivationResponse();
        serviceActivationResponse.setResultCode(svcActvaResponse.getCommonHeader().getResultCode());
        serviceActivationResponse.setResultMsg(svcActvaResponse.getCommonHeader().getResultMsg());

        return serviceActivationResponse;
    }

    @Override
    public ServiceStatusChangeResponse requestStatusChange(String serviceContractNo, ServiceStatusChangeRequest.Command command) {
        ContSttusChgRequest contSttusChgRequest = new ContSttusChgRequest();
        CommonHeader        commonHeader        = new CommonHeader();
        ContSttusChgReqInfo contSttusChgReqInfo = new ContSttusChgReqInfo();

        commonHeader.setCmpnCd(CommHeaderType.CMPN_CD_HOM.getCode());
        commonHeader.setMsgId(createMsgId());
        commonHeader.setMsgType(MsgType.CONTSTTUSCHGREQ.getCode());
        commonHeader.setChType(CommHeaderType.CH_TYPE.getCode());
        commonHeader.setResultMsg("");
        commonHeader.setResultCode("");

        contSttusChgReqInfo.setSvccontno(serviceContractNo);
        contSttusChgReqInfo.setContsttuschgcd(command.getCode()); //SUS : 정지, RSP : 정지복구
        contSttusChgReqInfo.setContsttuschgrsncd("CR");// 고객 요청

        contSttusChgRequest.setCommonHeader(commonHeader);
        contSttusChgRequest.setContSttusChgReqInfo(contSttusChgReqInfo);

        ServiceStatusChangeResponse serviceStatusChangeResponse;

        ContSttusChgResponse contSttusChgResponse = bssIotSenderWebService.contSttusChg(contSttusChgRequest);

        serviceStatusChangeResponse = new ServiceStatusChangeResponse();
        serviceStatusChangeResponse.setResultCode(contSttusChgResponse.getCommonHeader().getResultCode());
        serviceStatusChangeResponse.setResultMsg(contSttusChgResponse.getCommonHeader().getResultMsg());
        return serviceStatusChangeResponse;
    }

    private String createMsgId() {

        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }
}
