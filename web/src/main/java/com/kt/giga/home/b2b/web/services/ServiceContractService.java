package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.annotation.PrivacyAuditHistoric;
import com.kt.giga.home.b2b.domain.DeviceStatus;
import com.kt.giga.home.b2b.domain.SvcContractInfo;
import com.kt.giga.home.b2b.domain.UserInformation;
import com.kt.giga.home.b2b.domain.bssiot.ResponseType;
import com.kt.giga.home.b2b.domain.bssiot.ServiceActivationResponse;
import com.kt.giga.home.b2b.domain.bssiot.ServiceStatusChangeRequest;
import com.kt.giga.home.b2b.domain.bssiot.ServiceStatusChangeResponse;
import com.kt.giga.home.b2b.domain.integration.api.*;
import com.kt.giga.home.b2b.entity.ServiceContract;
import com.kt.giga.home.b2b.mapper.ServiceContractMapper;
import com.kt.giga.home.b2b.mapper.ServiceContractUnmaskedMapper;
import com.kt.giga.home.b2b.repository.ServiceContractRepository;
import com.kt.giga.home.b2b.repository.ServiceContractSpecifications;
import com.kt.giga.home.b2b.service.IBssIotSenderService;
import com.kt.giga.home.b2b.service.api.IMapUserCtnService;
import com.kt.giga.home.b2b.service.api.IProvisionService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.WebServiceClientException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * com.kt.giga.home.b2b.web.services
 * <p>
 * Created by cecil on 2017. 2. 25..
 */
@Slf4j
@Service
public class ServiceContractService implements IServiceContractService {

    @Autowired
    private ServiceContractRepository serviceContractRepository;

    @Autowired
    private ServiceContractMapper serviceContractMapper;

    @Autowired
    private ServiceContractUnmaskedMapper serviceContractUnmaskedMapper;

    @Autowired
    private IProvisionService iProvisionService;

    @Autowired
    private IMapUserCtnService mapUserCtnService;

    @Autowired
    private IBssIotSenderService bssIotSenderService;

    @Autowired
    private ISmsService smsService;


    @Override
    public SvcContractInfo getMaskedServiceContractInfo(Long svcContSerial) {

        ServiceContract serviceContract = serviceContractRepository.findOneBySerialNumber(svcContSerial);
        return serviceContractMapper.toSvcContractInfo(serviceContract);
    }

    @Override
    @PrivacyAuditHistoric(menuPath = "사용자관리 > 단말상세", unmasked = true, returnClass = SvcContractInfo.class, targetInfo = "유심일련번호, 단말일련번호, 상세주소, 사용자명, 휴대폰번호")
    public SvcContractInfo getUnmaskedServiceContractInfo(Long svcContSerial) {

        ServiceContract serviceContract = serviceContractRepository.findOneBySerialNumber(svcContSerial);
        return serviceContractUnmaskedMapper.toSvcContractInfo(serviceContract);
    }

    @Override
    public void updateSvcContractInfo(Long serviceContractSerial, UserInformation userInformation) throws RuntimeException {

        ServiceContract serviceContract = serviceContractRepository.findOneBySerialNumber(serviceContractSerial);

        // 활성화 상태 변경 여부
        boolean toggleActive = userInformation.getStatus() != serviceContract.getIsActive();

        // 사용자 전화번호 변경 여부
        boolean changingUserCtn = (StringUtils.isBlank(userInformation.getUserCtn()) ? StringUtils.isBlank(serviceContract
                .getUserCtn()) : !userInformation
                .getUserCtn()
                .equals(serviceContract.getUserCtn()));

        log.debug("Changing User Ctn : {}", changingUserCtn);
        // 최초 전화번호 등록 여부
        boolean isInitialUserCtn = changingUserCtn && StringUtils.isBlank(serviceContract.getUserCtn());
        log.debug("isInitialUserCtn : {}", isInitialUserCtn);


        // 이름 변경 여부
        boolean changingUserName = (StringUtils.isBlank(userInformation.getUserName()) ? StringUtils.isBlank(serviceContract
                .getUserName()) : !userInformation
                .getUserName()
                .equals(serviceContract.getUserName()));
        log.debug("changingUserName : {}", changingUserName);

        // 주소 변경 여부
        boolean changingAddress = (StringUtils.isBlank(userInformation.getDevicePlace()) ? StringUtils.isBlank(serviceContract
                .getDevicePlace()) : !userInformation
                .getDevicePlace()
                .equals(serviceContract.getDevicePlace()));
        log.debug("changingAddress : {}", changingAddress);

        // 사용자 전화번호를 제외한 정보 변경 여부
        boolean changingUserInfo = changingUserName || changingAddress;
        log.debug("changingUserInfo : {}", changingUserInfo);

        // 1. Validation
        validateUpdateServiceContract(serviceContract, userInformation.getUserCtn(), userInformation.getStatus(), changingUserCtn);

        if (changingAddress) {
            serviceContract.setDevicePlace(StringUtils.isBlank(userInformation.getDevicePlace()) ? null : userInformation
                    .getDevicePlace());
            serviceContractRepository.save(serviceContract);
        }

        if (changingUserName) {
            serviceContract.setUserName(StringUtils.isBlank(userInformation.getUserName()) ? null : userInformation.getUserName());
            serviceContractRepository.save(serviceContract);
        }

        // 사용으로 입력된 경우
        if (userInformation.getStatus()) {

            // 2. 사용자 등록하는 경우에 API로 Provision 안되어 있으면 API로 Provision
            if (!serviceContract.getDeviceStatus()
                                .getStatusCd()
                                .equals(DeviceStatus.PROVISION_COMPLETED.getStatusCd()) && isInitialUserCtn) {

                Long svcTgtSeq = requestProvisionToApi(
                        serviceContract.getCtn(),
                        String.valueOf(serviceContractSerial),
                        serviceContract.getIccId(),
                        serviceContract.getImei(),
                        serviceContract.getSecretKey(),
                        serviceContract.getDeviceModel().getName(),
                        serviceContract.getDeviceModel().getCode(),
                        ProvisionRequest.GwCode.CREATE
                );

                serviceContract.setSvcTgtSeq(svcTgtSeq);
                serviceContract.setSpotDevSeq(1L);
                serviceContract.setDeviceStatus(DeviceStatus.PROVISION_COMPLETED);

                serviceContractRepository.save(serviceContract);
            } else {

                //Provision은 되어 있으나 이용정지에서 사용으로 변경한 경우
                if (serviceContract.getIsSuspended()) {
                    requestProvisionToApi(
                            serviceContract.getCtn(),
                            String.valueOf(serviceContractSerial),
                            serviceContract.getIccId(),
                            serviceContract.getImei(),
                            serviceContract.getSecretKey(),
                            serviceContract.getDeviceModel().getName(),
                            serviceContract.getDeviceModel().getCode(),
                            ProvisionRequest.GwCode.RESUME
                    );

                    serviceContract.setIsSuspended(false);

                    serviceContractRepository.save(serviceContract);

                    //Activation되어 있으나 이용정지에서 사용으로 변경한 경우
                    if (serviceContract.getIsActive()) {
                        //BSS-IoT로 정지 복구 요청
                        requestResumeToBssIoT(String.valueOf(serviceContract.getSerialNumber()));
                    }
                }

            }

            boolean isUserMapped = false;

            // 3. API로 MapUser 전송 (MapUser 안되어 있는 경우)
            if (!serviceContract.getIsUserMapped()
                    && serviceContract.getDeviceStatus()
                                      .getStatusCd()
                                      .equals(DeviceStatus.PROVISION_COMPLETED.getStatusCd())) {

                String authenticationNumber = requestRegisterUserAuthToApi(
                        userInformation.getUserCtn().replaceAll("-", ""),
                        serviceContract.getAuthNumber(),
                        String.valueOf(serviceContract.getSvcTgtSeq())
                );

                serviceContract.setIsUserMapped(true);
                serviceContract.setUserCtn(userInformation.getUserCtn());

                if (StringUtils.isBlank(serviceContract.getAuthNumber())
                        || !authenticationNumber.equals(serviceContract.getAuthNumber())) {

                    serviceContract.setAuthNumber(authenticationNumber);

                }

                serviceContractRepository.save(serviceContract);

                isUserMapped = true;
            }

            // 4. BssIoT로 Activation 전송
            if (!serviceContract.getIsActive()
                    && serviceContract.getIsUserMapped()
                    && serviceContract.getDeviceStatus()
                                      .getStatusCd()
                                      .equals(DeviceStatus.PROVISION_COMPLETED.getStatusCd())) {

                requestActivationToBssIoT(String.valueOf(serviceContract.getSerialNumber()));

                serviceContract.setIsActive(true);
                serviceContract.setActivated(LocalDateTime.now());
                serviceContractRepository.save(serviceContract);

            }

            // 5. Activation되어 있으나 일시정지에서 사용으로 변경하는 경우
            if (serviceContract.getIsActive()
                        && serviceContract.getIsSuspended()) {
                //BSS-IoT로 정지 복구 요청
                requestResumeToBssIoT(String.valueOf(serviceContract.getSerialNumber()));

                serviceContract.setIsSuspended(false);
                serviceContractRepository.save(serviceContract);
            }

            // 6. SMS 발송
            if (!StringUtils.isBlank(serviceContract.getUserCtn())
                    && serviceContract.getIsActive()
                    && isUserMapped) {

                sendSms(userInformation.getUserCtn(), serviceContract.getAuthNumber());
            }

        } else {
            //일시 정지로 입력된 경우

            //일시정지로 변경하는 경우
            if (!serviceContract.getIsSuspended()
                    && serviceContract.getIsUserMapped()
                    && serviceContract.getDeviceStatus()
                                      .getStatusCd()
                                      .equals(DeviceStatus.PROVISION_COMPLETED.getStatusCd())) {
                // API로 일시정지 Provision
                requestProvisionToApi(
                        serviceContract.getCtn(),
                        String.valueOf(serviceContractSerial),
                        serviceContract.getIccId(),
                        serviceContract.getImei(),
                        serviceContract.getSecretKey(),
                        serviceContract.getDeviceModel().getName(),
                        serviceContract.getDeviceModel().getCode(),
                        ProvisionRequest.GwCode.SUSPEND
                );

                serviceContract.setIsSuspended(true);

                serviceContractRepository.save(serviceContract);
            }
        }
    }

    private void sendSms(String userCtn, String authenticationNumber) {

        String title   = "LTE-M 인증번호";
        String message = "LTE-M 인증번호는 [" + authenticationNumber + "] 입니다.";
        String sendTo  = userCtn.replaceAll("-", "");

        log.info("Sending authentication SMS to User App with mobile number : " + sendTo);


        // SMS 발송
        Optional<String> tranctionId = smsService.sendMessage("100", sendTo, title, message);

        if (!tranctionId.isPresent()) {
            throw new RuntimeException("SMS 발송에 실패하였습니다.");
        }

    }

    private void requestActivationToBssIoT(String serviceContractNumber) {
        try {
            ServiceActivationResponse activationResponse = bssIotSenderService.serviceActivation(serviceContractNumber);


            if (!activationResponse.getResultCode().equals(ResponseType.SUCCESS.getCode())) {
                throw new RuntimeException("Failed to request to BSSIoT - Response : " + activationResponse);
            }

        } catch (WebServiceClientException e) {
            throw new RuntimeException("Failed to request to BSSIoT.", e);
        }
    }

    private void requestSuspendToBssIoT(String serviceContractNumber) {
        requestStatusChangeToBssIot(serviceContractNumber, ServiceStatusChangeRequest.Command.SUSPEND);
    }

    private void requestStatusChangeToBssIot(String serviceContractNumber, ServiceStatusChangeRequest.Command command) {
        try {
            ServiceStatusChangeResponse deactivationResponse = bssIotSenderService.requestStatusChange(serviceContractNumber, command);


            if (!deactivationResponse.getResultCode().equals(ResponseType.SUCCESS.getCode())) {
                throw new RuntimeException("Failed to request to BSSIoT - Response : " + deactivationResponse);
            }

        } catch (WebServiceClientException e) {
            throw new RuntimeException("Failed to request to BSSIoT.", e);
        }
    }

    private void requestResumeToBssIoT(String serviceContractNumber) {
        requestStatusChangeToBssIot(serviceContractNumber, ServiceStatusChangeRequest.Command.RESUME);
    }

    private String requestRegisterUserAuthToApi(String userCtn, String authNumber, String svcTgtSeq) {
        return requestUserToApi(userCtn, authNumber, svcTgtSeq, MapUserCtnRequest.RequestType.REGISTER);
    }

    private String requestUpdateUserAuthToApi(String userCtn, String authNumber, String svcTgtSeq) {
        return requestUserToApi(userCtn, authNumber, svcTgtSeq, MapUserCtnRequest.RequestType.UPDATE);
    }

    private String requestDeleteUserAuthToApi(String userCtn, String authNumber, String svcTgtSeq) {
        return requestUserToApi(userCtn, authNumber, svcTgtSeq, MapUserCtnRequest.RequestType.DEREGISTER);
    }

    private String requestUserToApi(String userCtn, String authNumber, String svcTgtSeq, MapUserCtnRequest.RequestType requestType) {
        String authenticationNumber = StringUtils.isBlank(authNumber) ? RandomStringUtils.randomNumeric(6) : authNumber;

        MapUserCtnRequest mapUserCtnRequest = new MapUserCtnRequest(svcTgtSeq, userCtn.replaceAll("-", ""), authenticationNumber, requestType);

        try {
            MapUserCtnResponse mapUserCtnResponse = mapUserCtnService.request(mapUserCtnRequest);

            if (!(MapUserCtnResponse.Result.SUCCESS == mapUserCtnResponse.getResult())) {
                throw new RuntimeException("Failed to request to API - Response : " + mapUserCtnResponse);
            }

            return authenticationNumber;

        } catch (FeignException e) {
            throw new RuntimeException("Failed to request to API.", e);
        }
    }

    private Long requestProvisionToApi(String deviceCtn, String serviceContractSerial, String iccid, String imei, String secretKey, String modelName, String modelCode, ProvisionRequest.GwCode provisionType) {

        ProvisionRequest provisionRequest = new ProvisionRequest(
                serviceContractSerial
                , iccid
                , "C_" + imei  //홈매니저 API C_붙여서 imei전송
                , deviceCtn.replaceAll("-", "")
                , secretKey
                , modelName
                , modelCode
                , provisionType
        );

        try {
            ProvisionResponse provisionResponse = iProvisionService.request(provisionRequest);

            if (!(ProvisionResponse.Result.SUCCESS == provisionResponse.getResult())) {
                throw new RuntimeException("Failed to request to API - Response : " + provisionResponse);
            }

            return Long.valueOf(provisionResponse.getServiceTargetSequence());

        } catch (FeignException e) {
            throw new RuntimeException("Failed to request to API.", e);
        }
    }

    @Override
    @PrivacyAuditHistoric(menuPath = "사용자관리 > 단말목록", downloaded = true, isList = true, returnClass = SvcContractInfo.class, targetInfo = "서비스계약번호, 단말번호, 상세주소, 사용자명, 전화번호, 인증유무")
    public List<SvcContractInfo> getSvcContractInfosToExcel(List<Long> svcContSerials) {

        List<ServiceContract> serviceContracts = serviceContractRepository.findBySerialNumberIn(svcContSerials);

        return serviceContractMapper.toSvcContractInfoList(serviceContracts);
    }

    @Override
    @PrivacyAuditHistoric(menuPath = "사용자관리 > 단말목록", unmasked = true, returnClass = SvcContractInfo.class, targetInfo = "상세주소, 사용자명, 휴대폰번호")
    public SvcContractInfo getUnmaskedSvcContractInfos(Long svcContSerial) {

        List<ServiceContract> serviceContracts = serviceContractRepository.findBySerialNumberIn(Collections.singletonList(svcContSerial));

        return serviceContractUnmaskedMapper.toSvcContractInfo(serviceContracts.get(0));
    }

    @Override
    public Page<ServiceContract> getServiceContracts(String serialNumber, String statusCode, String searchKey, String searchText, Pageable pageable) {
        Specifications<ServiceContract> specifications = Specifications.where(ServiceContractSpecifications.equalsBizContSerial(serialNumber));

        // 사용, 정지, 미등록 검색
        if (StringUtils.isNotBlank(statusCode)) {

        }

        // 서비스계약번호, 사용자명, 휴대폰번호, 유심일련번호, 단말일련번호 검색
        if (StringUtils.isNotBlank(searchText)) {
            // 서비스계약번호
            if ("svcContSerial".equals(searchKey)) {
                try {
                    specifications = specifications.and(ServiceContractSpecifications.equalsSvcContSerial(Long.parseLong(searchText)));
                } catch (NumberFormatException ignored) {
                }
            }
            // 사용자명
            if ("userName".equals(searchKey)) {
                specifications = specifications.and(ServiceContractSpecifications.equalsUserName(searchText));
            }
            // 휴대폰번호
            if ("userCtn".equals(searchKey)) {
//                specifications = specifications.and(ServiceContractSpecifications.equalsCtn(cryptoService.encrypt(searchText)));
                specifications = specifications.and(ServiceContractSpecifications.equalsCtn(searchText));
            }
            // 유심일련번호
            if ("iccId".equals(searchKey)) {
//                specifications = specifications.and(ServiceContractSpecifications.equalsIccId(cryptoService.encrypt(searchText)));
                specifications = specifications.and(ServiceContractSpecifications.equalsIccId(searchText));
            }
            // 단말일련번호
            if ("imei".equals(searchKey)) {
//                specifications = specifications.and(ServiceContractSpecifications.equalsImei(cryptoService.encrypt(searchText)));
                specifications = specifications.and(ServiceContractSpecifications.equalsImei(searchText));
            }
        }

        return serviceContractRepository.findAll(specifications, pageable);
    }

    private void validateUpdateServiceContract(ServiceContract serviceContract, String userCtn, Boolean activationStatus, boolean changingUserCtn) {

        DeviceStatus currentDeviceStatus = serviceContract.getDeviceStatus();

        // DMS 로부터 장치 정보 수신 전인 경우
        if (changingUserCtn && serviceContract.getDeviceStatus()
                                              .getStatusCd() < DeviceStatus.PROVISION_RECEIVED.getStatusCd()) {
            throw new IllegalStateException("인증 정보 미수신 상태에서는 전화번호 등록이 불가능 합니다.");
        }

        // 이미 매핑된 이용자 전화번호가 있는 경우
        if (changingUserCtn && serviceContract.getIsUserMapped() && serviceContract.getIsActive()) {
            throw new IllegalStateException("사용자가 등록되어 있는 상태에서는 사용자 전화번호 변경이 불가능합니다. 사용자 해제 후 다시 등록하세요.");
        }

        // 계약 만료 상태인 경우
        if (currentDeviceStatus.getStatusCd() >= DeviceStatus.EXPIRATION_RECEIVED.getStatusCd()) {
            throw new IllegalStateException("만료된 계약은 수정이 불가능합니다.");
        }

        // 사용자 등록이 안된 상태에서 일시정지로 등록하는 경우
        if (changingUserCtn && !activationStatus) {
            throw new IllegalStateException("일시정지는 사용자 등록 후 가능합니다.");
        }

    }


    List<SvcContractInfo> getSvcContractInfos(List<ServiceContract> serviceContracts) {

        return serviceContractMapper.toSvcContractInfoList(serviceContracts);
    }

    @Override
    public void unmapUser(Long serviceContractSerial) {


        ServiceContract serviceContract = serviceContractRepository.findOneBySerialNumber(serviceContractSerial);

        // DMS 로부터 장치 정보 수신 전인 경우
        if (serviceContract.getDeviceStatus().getStatusCd() < DeviceStatus.PROVISION_RECEIVED.getStatusCd()) {
            throw new IllegalStateException("현재 상태에서는 수정이 불가능합니다. 장치 인증 정보가 존재하지 않습니다.");
        }

        // 계약 만료 상태인 경우
        if (serviceContract.getDeviceStatus().getStatusCd() >= DeviceStatus.EXPIRATION_RECEIVED.getStatusCd()) {
            throw new IllegalStateException("만료된 계약은 수정이 불가능합니다.");
        }

        String svcTgtSeq = String.valueOf(serviceContract.getSvcTgtSeq());

        //API로 사용자 정보 요청한 상태면 사용자 정보 취소
        if (serviceContract.getIsUserMapped()) {
            if (serviceContract.getIsCertify()) {
                requestUnmapUserToApi(svcTgtSeq);
                serviceContract.setIsCertify(false);
            } else {
                requestDeleteUserAuthToApi(serviceContract.getUserCtn(), serviceContract.getAuthNumber(), String.valueOf(serviceContract
                        .getSvcTgtSeq()));
            }

            serviceContract.setIsUserMapped(false);
            serviceContract.setSvcTgtSeq(null);
            serviceContract.setSpotDevSeq(null);
            serviceContract.setUserCtn(null);
        }

        //API로 프로비저닝 해지 요청
        Long svcTgtSeqLong = requestProvisionToApi(
                serviceContract.getCtn(),
                String.valueOf(serviceContractSerial),
                serviceContract.getIccId(),
                serviceContract.getImei(),
                serviceContract.getSecretKey(),
                serviceContract.getDeviceModel().getName(),
                serviceContract.getDeviceModel().getCode(),
                ProvisionRequest.GwCode.CLOSE
        );

        serviceContract.setSvcTgtSeq(null);
        serviceContract.setSpotDevSeq(null);
        serviceContract.setIsSuspended(false);
        serviceContract.setAuthNumber(null);
        serviceContract.setUserName(null);
        serviceContract.setDevicePlace(null);
        serviceContract.setDeviceStatus(DeviceStatus.PROVISION_RECEIVED);

        serviceContractRepository.save(serviceContract);


        //정지 상태가 아니면 BSS-IoT로 정지 요청
        if (serviceContract.getIsActive() && !serviceContract.getIsSuspended()) {
            requestSuspendToBssIoT(String.valueOf(serviceContract.getSerialNumber()));
            serviceContract.setIsSuspended(true);
        }

        serviceContractRepository.save(serviceContract);
    }

    @Override
    public void resendUserAuthentication(Long serviceContractNo) {

        ServiceContract serviceContract = serviceContractRepository.findOneBySerialNumber(serviceContractNo);

        String authenticationNumber = requestUpdateUserAuthToApi(
                serviceContract.getUserCtn().replaceAll("-", ""),
                serviceContract.getAuthNumber(),
                String.valueOf(serviceContract.getSvcTgtSeq())
        );

        serviceContract.setIsUserMapped(true);
        serviceContract.setAuthNumber(authenticationNumber);

        serviceContractRepository.save(serviceContract);

        // 5. SMS 발송
        if (!StringUtils.isBlank(serviceContract.getUserCtn())) {

            sendSms(serviceContract.getUserCtn(), serviceContract.getAuthNumber());
        }

    }

    private void requestUnmapUserToApi(String svcTgtSeq) {
        try {
            MapUserCtnResponse unmapResponse = mapUserCtnService.unmap(new UnmapUserCtnRequest(String.valueOf(svcTgtSeq)));

            if (!(MapUserCtnResponse.Result.SUCCESS == unmapResponse.getResult())) {
                throw new RuntimeException("Failed to request to API - Response : " + unmapResponse);
            }

        } catch (FeignException e) {
            throw new RuntimeException("Failed to request to API.", e);
        }
    }

}

