package com.kt.giga.home.b2b.core.bssiot.service;


import com.kt.giga.home.b2b.core.service.BizContractService;
import com.kt.giga.home.b2b.core.service.IServiceContractService;
import com.kt.giga.home.b2b.domain.bssiot.CommHeaderType;
import com.kt.giga.home.b2b.domain.bssiot.MsgType;
import com.kt.giga.home.b2b.domain.bssiot.ResponseType;
import com.kt.giga.home.b2b.domain.bssiot.receiver.*;
import com.kt.giga.home.b2b.domain.exceptions.BizContractAlreadyExistException;
import com.kt.giga.home.b2b.domain.exceptions.BizContractNotFoundException;
import com.kt.giga.home.b2b.domain.exceptions.ServiceContractAlreadyExistException;
import com.kt.giga.home.b2b.domain.exceptions.ServiceContractNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebService;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;

/**
 * Created by 민우 on 2016-12-06.
 */
@Slf4j(topic = "com.kt.giga.home.b2b.core.bssiot")
@WebService(endpointInterface = "com.kt.giga.home.b2b.core.bssiot.service.IBssIotService", name = "PS_SVCResProviderPort", targetNamespace = "http://www.kt.com/m2m/provider/svcResProvider")
@Service
public class BssIotService implements IBssIotService {

    @Autowired
    private Validator validator;

    @Autowired
    private BizContractService bizContractService;

    @Autowired
    private IServiceContractService serviceContractService;

    @Override
    public BizContractResponse bizContInfoRst(BizContractRequest bizContractRequest) {

        log.info("Request from BSSIoT : {}", bizContractRequest);

        Set<ConstraintViolation<BizContractRequest>> constraintViolations = validate(bizContractRequest);

        Optional<ConstraintViolation<BizContractRequest>> firstViolation = constraintViolations.stream()
                .findFirst();

        BizContractResponse response;

        CommonHeader requestHeader = bizContractRequest.getCommonHeader();


        if (firstViolation.isPresent()) {
            response = getInvalidResponse(requestHeader, firstViolation
                    .get(), new BizContractResponse());

        } else {

            try {
                bizContractService.insertBizContract(bizContractRequest.getBssIotBizContractDto());

                response = new BizContractResponse(new CommonHeader(CommHeaderType.CMPN_CD_HOM, requestHeader.getMsgId(), MsgType.BIZCONTINFORES, requestHeader
                        .getUserId(), requestHeader.getChType(), ResponseType.SUCCESS));
            } catch (BizContractAlreadyExistException e) {
                log.error(e.getMessage());

                response = new BizContractResponse(new CommonHeader(CommHeaderType.CMPN_CD_HOM, requestHeader.getMsgId(), MsgType.BIZCONTINFORES, requestHeader
                        .getUserId(), requestHeader.getChType(), ResponseType.EXISTENT_TGTCD));
            }
        }

        log.info("Response to BSSIoT : {}", response);

        return response;
    }


    private <T extends BssIotBase, U extends BssIotBase> T getInvalidResponse(CommonHeader requestCommonHeader, ConstraintViolation<U> violation, T t) {
        CommonHeader responseCommonHeader = getResponseCommonHeader(requestCommonHeader, violation, t);

        t.setCommonHeader(responseCommonHeader);
        return t;
    }

    private <T extends BssIotBase, U extends BssIotBase> CommonHeader getResponseCommonHeader(CommonHeader requestCommonHeader, ConstraintViolation<U> violation, BssIotBase response) {

        MsgType msgType;

        if (response instanceof BizContractResponse)
            msgType = MsgType.BIZCONTINFORES;
        else if (response instanceof ContSttusChgRstResponse)
            msgType = MsgType.CONTSTTUSCHGRSTRES;
        else if (response instanceof PreOpenRstResponse)
            msgType = MsgType.PREOPENRES;
        else if (response instanceof UseQntRstResponse)
            msgType = MsgType.USEQNTRES;
        else
            throw new IllegalArgumentException("Unexpected response type : " + response.getClass().getName());

        switch (violation.getMessage()) {
            case "length":
                return new CommonHeader(CommHeaderType.CMPN_CD_HOM, requestCommonHeader.getMsgId(), msgType, requestCommonHeader
                        .getUserId(), requestCommonHeader.getChType(), ResponseType.INVALID_LENGTH);
            case "empty":
                return new CommonHeader(CommHeaderType.CMPN_CD_HOM, requestCommonHeader.getMsgId(), msgType, requestCommonHeader
                        .getUserId(), requestCommonHeader.getChType(), ResponseType.MISSING_VALUE);
            case "value":
                return new CommonHeader(CommHeaderType.CMPN_CD_HOM, requestCommonHeader.getMsgId(), msgType, requestCommonHeader
                        .getUserId(), requestCommonHeader.getChType(), ResponseType.UNDEFINED_VALUE);
            default:
                return new CommonHeader(CommHeaderType.CMPN_CD_HOM, requestCommonHeader.getMsgId(), msgType, requestCommonHeader
                        .getUserId(), requestCommonHeader.getChType(), ResponseType.HOME_ERROR);
        }
    }

    @Override
    public ContSttusChgRstResponse contSttusChgRst(ContSttusChgRstRequest contSttusChgRstRequest) {

        log.info("Request from BSSIoT : {}", contSttusChgRstRequest);

        Set<ConstraintViolation<ContSttusChgRstRequest>> constraintViolations = validate(contSttusChgRstRequest);
        Optional<ConstraintViolation<ContSttusChgRstRequest>> firstViolation = constraintViolations.stream().findFirst();

        ContSttusChgRstResponse response;

        CommonHeader requestHeader = contSttusChgRstRequest.getCommonHeader();

        if (firstViolation.isPresent()) {
            response = getInvalidResponse(requestHeader, firstViolation
                    .get(), new ContSttusChgRstResponse());

        } else {

            try {
                bizContractService.changeBizContract(contSttusChgRstRequest.getContSttusChgRstReqInfo());

                response = new ContSttusChgRstResponse(new CommonHeader(CommHeaderType.CMPN_CD_HOM, requestHeader.getMsgId(), MsgType.CONTSTTUSCHGRSTRES, requestHeader
                        .getUserId(), requestHeader.getChType(), ResponseType.SUCCESS));
            } catch (BizContractAlreadyExistException e) {
                log.error(e.getMessage());

                response = new ContSttusChgRstResponse(new CommonHeader(CommHeaderType.CMPN_CD_HOM, requestHeader.getMsgId(), MsgType.CONTSTTUSCHGRSTRES, requestHeader
                        .getUserId(), requestHeader.getChType(), ResponseType.EQUAL_CD));
            } catch (BizContractNotFoundException e) {
                log.error(e.getMessage());

                response = new ContSttusChgRstResponse(new CommonHeader(CommHeaderType.CMPN_CD_HOM, requestHeader.getMsgId(), MsgType.CONTSTTUSCHGRSTRES, requestHeader
                        .getUserId(), requestHeader.getChType(), ResponseType.NOT_BIZCD));
            }
        }

        log.info("Response to BSSIoT : {}", response);

        return response;
    }

    @Override
    public PreOpenRstResponse preOpenRst(PreOpenRstRequest preOpenRstRequest) {

        log.info("Request from BSSIoT : {}", preOpenRstRequest);

        Set<ConstraintViolation<PreOpenRstRequest>> constraintViolations = validate(preOpenRstRequest);

        Optional<ConstraintViolation<PreOpenRstRequest>> firstViolation = constraintViolations.stream()
                .findFirst();

        PreOpenRstResponse response;

        CommonHeader requestHeader = preOpenRstRequest.getCommonHeader();


        if (firstViolation.isPresent()) {
            response = getInvalidResponse(requestHeader, firstViolation
                    .get(), new PreOpenRstResponse());

        } else {

            try {
                serviceContractService.insertServiceContracts(preOpenRstRequest.getPreOpenRstReqInfo());

                response = new PreOpenRstResponse(new CommonHeader(CommHeaderType.CMPN_CD_HOM, requestHeader.getMsgId(), MsgType.PREOPENRES, requestHeader
                        .getUserId(), requestHeader.getChType(), ResponseType.SUCCESS));
            } catch (ServiceContractAlreadyExistException e) {
                log.error(e.getMessage());

                response = new PreOpenRstResponse(new CommonHeader(CommHeaderType.CMPN_CD_HOM, requestHeader.getMsgId(), MsgType.PREOPENRES, requestHeader
                        .getUserId(), requestHeader.getChType(), ResponseType.EXISTENT_TGTCD));
            } catch (BizContractNotFoundException e) {
                log.error(e.getMessage());

                response = new PreOpenRstResponse(new CommonHeader(CommHeaderType.CMPN_CD_HOM, requestHeader.getMsgId(), MsgType.CONTSTTUSCHGRSTRES, requestHeader
                        .getUserId(), requestHeader.getChType(), ResponseType.NOT_BIZCD));
            }
        }

        log.info("Response to BSSIoT : {}", response);

        return response;
    }


    @Override
    public UseQntRstResponse useQntRst(UseQntRstRequest useQntRstRequest) {

        log.info("Request from BSSIoT : {}", useQntRstRequest);

        Set<ConstraintViolation<UseQntRstRequest>> constraintViolations = validate(useQntRstRequest);

        Optional<ConstraintViolation<UseQntRstRequest>> firstViolation = constraintViolations.stream()
                .findFirst();

        UseQntRstResponse response;

        CommonHeader requestHeader = useQntRstRequest.getCommonHeader();


        if (firstViolation.isPresent()) {
            response = getInvalidResponse(requestHeader, firstViolation
                    .get(), new UseQntRstResponse());

        } else {

            try {
                bizContractService.insertUsage(useQntRstRequest.getUseQntRstReqInfo());

                response = new UseQntRstResponse(new CommonHeader(CommHeaderType.CMPN_CD_HOM, requestHeader.getMsgId(), MsgType.PREOPENRES, requestHeader
                        .getUserId(), requestHeader.getChType(), ResponseType.SUCCESS));
            } catch (ServiceContractNotFoundException e) {
                log.error(e.getMessage());

                response = new UseQntRstResponse(new CommonHeader(CommHeaderType.CMPN_CD_HOM, requestHeader.getMsgId(), MsgType.PREOPENRES, requestHeader
                        .getUserId(), requestHeader.getChType(), ResponseType.UNREGISTERED_SERVICE));
            } catch (BizContractNotFoundException e) {
                log.error(e.getMessage());

                response = new UseQntRstResponse(new CommonHeader(CommHeaderType.CMPN_CD_HOM, requestHeader.getMsgId(), MsgType.CONTSTTUSCHGRSTRES, requestHeader
                        .getUserId(), requestHeader.getChType(), ResponseType.NOT_BIZCD));
            }
        }

        log.info("Response to BSSIoT : {}", response);

        return response;

    }

    private <T> Set<ConstraintViolation<T>> validate(T t) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
        constraintViolations.forEach(v -> log.info("{} : {}", v.getClass().getName(), v));
        return constraintViolations;

    }

}
