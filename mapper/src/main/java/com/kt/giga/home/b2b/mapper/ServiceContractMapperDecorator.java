package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.SvcContractInfo;
import com.kt.giga.home.b2b.entity.ServiceContract;
import com.kt.giga.home.b2b.service.IMaskingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alsdnfl on 2017-01-25.
 */
@Slf4j
public abstract class ServiceContractMapperDecorator implements ServiceContractMapper {

    @Autowired
    private IMaskingService iMaskingService;

    @Autowired
    @Qualifier("delegate")
    private ServiceContractMapper serviceContractMapper;

    /*@Override
    public ServiceContract toServiceContract(PreOpenRstLs dto, BizContract bizContract, DeviceModel deviceModel, boolean active){
        ServiceContract serviceContract = serviceContractMapper.toServiceContract(dto, bizContract, deviceModel, active);
        serviceContract.setIccId(cryptoService.encrypt(dto.getIccid()));
        serviceContract.setImei(cryptoService.encrypt(dto.getImei()));

        return serviceContract;
    }*/

    @Override
    public List<SvcContractInfo> toSvcContractInfoList(List<ServiceContract> serviceContracts) {

        if ( serviceContracts == null ) {
            return null;
        }

        List<SvcContractInfo> list = new ArrayList<>();
        serviceContracts.forEach(serviceContract -> list.add(this.toSvcContractInfo(serviceContract)));

        return list;
    }

    @Override
    public SvcContractInfo toSvcContractInfo(ServiceContract serviceContract) {

        if ( serviceContract == null ) {
            return null;
        }

        SvcContractInfo svcContractInfo = serviceContractMapper.toSvcContractInfo(serviceContract);
        svcContractInfo.setIccId(iMaskingService.maskAll(serviceContract.getIccId()));
        svcContractInfo.setImei(iMaskingService.maskAll(serviceContract.getImei()));

        if(StringUtils.isNotBlank(serviceContract.getDevicePlace()))
            svcContractInfo.setDevicePlace(iMaskingService.maskAll(serviceContract.getDevicePlace()));
        if(StringUtils.isNotBlank(serviceContract.getUserName()))
            svcContractInfo.setUserName(iMaskingService.maskNameOfPerson(serviceContract.getUserName()));
        if(StringUtils.isNotBlank(serviceContract.getUserCtn()))
            svcContractInfo.setUserCtn(iMaskingService.maskTelephone(serviceContract.getUserCtn()));

        svcContractInfo.setCurrentBizContract(serviceContractMapper.toContractInfo(serviceContract.getCurrentBizContract()));

        return svcContractInfo;
    }
}
