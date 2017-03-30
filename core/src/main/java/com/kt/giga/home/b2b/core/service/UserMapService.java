package com.kt.giga.home.b2b.core.service;

import com.kt.giga.home.b2b.core.dms.domain.DeviceResponse;
import com.kt.giga.home.b2b.core.dms.domain.ResponseType;
import com.kt.giga.home.b2b.entity.ServiceContract;
import com.kt.giga.home.b2b.repository.ServiceContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by alsdnfl on 2017-03-20.
 */
@Service
public class UserMapService implements IUserMapService {

    @Autowired
    private ServiceContractRepository serviceContractRepository;

    public DeviceResponse userMap(Long svcTgtSeq){

        ServiceContract serviceContract = serviceContractRepository.findOneBySvcTgtSeq(svcTgtSeq);

        DeviceResponse deviceResponse = new DeviceResponse();

        if (serviceContract == null){
            deviceResponse.setResultCode(ResponseType.FAIL.getCode());
            deviceResponse.setResultMsg(("svcTgtSeq doesn't exists."));
            return deviceResponse;
        }

        serviceContract.setIsCertify(true);
        serviceContractRepository.saveAndFlush(serviceContract);

        deviceResponse.setResultCode(ResponseType.SUCCESS.getCode());
        deviceResponse.setResultMsg(ResponseType.SUCCESS.getMsg());

        return deviceResponse;
    };
}
