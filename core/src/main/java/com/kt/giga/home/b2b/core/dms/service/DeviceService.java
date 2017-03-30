package com.kt.giga.home.b2b.core.dms.service;

import com.kt.giga.home.b2b.core.dms.domain.DeviceCloseRequest;
import com.kt.giga.home.b2b.core.dms.domain.DeviceOpenRequest;
import com.kt.giga.home.b2b.core.dms.domain.DeviceResponse;
import com.kt.giga.home.b2b.core.dms.domain.ResponseType;
import com.kt.giga.home.b2b.domain.DeviceStatus;
import com.kt.giga.home.b2b.domain.integration.api.MapUserCtnResponse;
import com.kt.giga.home.b2b.domain.integration.api.ProvisionRequest;
import com.kt.giga.home.b2b.domain.integration.api.ProvisionResponse;
import com.kt.giga.home.b2b.domain.integration.api.UnmapUserCtnRequest;
import com.kt.giga.home.b2b.entity.ServiceContract;
import com.kt.giga.home.b2b.repository.ServiceContractRepository;
import com.kt.giga.home.b2b.service.api.IMapUserCtnService;
import com.kt.giga.home.b2b.service.api.IProvisionService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 민우 on 2016-12-05.
 */
@Slf4j(topic = "com.kt.giga.home.b2b.core.dms")
@Service
public class DeviceService implements IDeviceService {

    @Autowired
    private ServiceContractRepository serviceContractRepository;

    @Autowired
    private IMapUserCtnService iMapUserCtnService;

    @Autowired
    private IProvisionService iProvisionService;


    public DeviceResponse openDevice(DeviceOpenRequest deviceOpenRequest) {

        ServiceContract serviceContract = serviceContractRepository.findOpenByImei(deviceOpenRequest.getImei(), DeviceStatus.CONTRACT_RECEIVED.getStatusCd());

        DeviceResponse deviceResponse = new DeviceResponse();

        if (serviceContract == null) {
            deviceResponse.setResultCode(ResponseType.FAIL.getCode());
            deviceResponse.setResultMsg("Imei doesn't exists.");
            return deviceResponse;
        }

        serviceContract.setDeviceStatus(DeviceStatus.PROVISION_RECEIVED);
        serviceContract.setSecretKey(deviceOpenRequest.getSecretKey());
        serviceContract.setCtn(deviceOpenRequest.getCtn());

        serviceContractRepository.saveAndFlush(serviceContract);

        log.info("Device Status changed : ServiceContract - {}", serviceContract);

        deviceResponse.setResultCode(ResponseType.SUCCESS.getCode());
        deviceResponse.setResultMsg(ResponseType.SUCCESS.getMsg());

        return deviceResponse;
    }

    public DeviceResponse closeDevice(DeviceCloseRequest deviceCloseRequest) {

        ServiceContract serviceContract = serviceContractRepository.findCloseByImei(deviceCloseRequest.getImei(), DeviceStatus.CONTRACT_RECEIVED.getStatusCd(), DeviceStatus.EXPIRATION_RECEIVED.getStatusCd());

        DeviceResponse deviceResponse = new DeviceResponse();

        if (serviceContract == null) {
            deviceResponse.setResultCode(ResponseType.FAIL.getCode());
            deviceResponse.setResultMsg("Imei doesn't exists.");
            return deviceResponse;
        }

        deviceResponse.setResultCode(ResponseType.SUCCESS.getCode());
        deviceResponse.setResultMsg(ResponseType.SUCCESS.getMsg());

        //사용자 매핑이 되어 있는 경우 -> API 사용자 맵핑해제(manager 테이블 로우 삭제, 인증번호 삭제는 날릴 필요 X)
        if (serviceContract.getSvcTgtSeq() != null && serviceContract.getIsCertify()) {
            if (requestDeviceCloseUnmapToApi(serviceContract)) return deviceResponse;
            serviceContract.setIsCertify(false);
            serviceContractRepository.saveAndFlush(serviceContract);
        }

        //해지 프로비저닝 요청
        if (serviceContract.getSvcTgtSeq() != null && serviceContract.getIsUserMapped()) {
            if (requestDeviceCloseProvisionToApi(serviceContract)) return deviceResponse;
            serviceContract.setIsUserMapped(false);
            serviceContractRepository.saveAndFlush(serviceContract);
        }

        serviceContract.setSvcTgtSeq(null);
        serviceContract.setSpotDevSeq(null);
        serviceContract.setUserCtn(null);
        serviceContract.setUserName(null);
        serviceContract.setDevicePlace(null);
        serviceContract.setIsCertify(false);
        serviceContract.setIsActive(false);
        serviceContract.setAuthNumber(null);
        serviceContract.setDeviceStatus(DeviceStatus.EXPIRED);
        serviceContractRepository.saveAndFlush(serviceContract);

        log.info("Device Status changed : ServiceContract - {}", serviceContract);

        return deviceResponse;
    }

    private boolean requestDeviceCloseProvisionToApi(ServiceContract serviceContract) {
        ProvisionRequest provisionRequest = new ProvisionRequest(
                String.valueOf(serviceContract.getSerialNumber())
                , serviceContract.getIccId()
                , "C_" + serviceContract.getImei()
                , serviceContract.getCtn().replaceAll("-", "")
                , serviceContract.getSecretKey()
                , serviceContract.getDeviceModel().getName()
                , serviceContract.getDeviceModel().getCode()
                , ProvisionRequest.GwCode.CLOSE
        );

        try {
            ProvisionResponse provisionResponse = iProvisionService.request(provisionRequest);

            if (!(ProvisionResponse.Result.SUCCESS == provisionResponse.getResult())) {
                log.error("iProvisionService response Result Code : {}", provisionResponse.getResult());
                log.error("HomeManager API return Fail");
                return true;
            }
        } catch (FeignException e) {
            log.error("HomeManager API return Fail :{}", e);
            return true;
        }
        return false;
    }

    private boolean requestDeviceCloseUnmapToApi(ServiceContract serviceContract) {
        UnmapUserCtnRequest unmapUserCtnRequest = new UnmapUserCtnRequest(
                serviceContract.getSvcTgtSeq().toString()
        );
        MapUserCtnResponse mapUserCtnResponse = iMapUserCtnService.unmap(unmapUserCtnRequest);
        if (!(MapUserCtnResponse.Result.SUCCESS == mapUserCtnResponse.getResult())) {
            log.error("IMapUserCtnService unmap response Result Code {}({}) svc_tgt_seq : {}", mapUserCtnResponse.getResult()
                    .getCode(), mapUserCtnResponse
                    .getResult()
                    .getDescription(), serviceContract.getSvcTgtSeq());
            log.error("HomeManager API return Fail");
            return true;
        }
        return false;
    }

}
