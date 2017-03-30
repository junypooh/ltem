package com.kt.giga.home.b2b.core.service;

import com.kt.giga.home.b2b.domain.bssiot.receiver.PreOpenRstLs;
import com.kt.giga.home.b2b.domain.bssiot.receiver.PreOpenRstReqInfo;
import com.kt.giga.home.b2b.domain.exceptions.BizContractNotFoundException;
import com.kt.giga.home.b2b.domain.exceptions.ServiceContractAlreadyExistException;
import com.kt.giga.home.b2b.entity.BizContract;
import com.kt.giga.home.b2b.entity.DeviceModel;
import com.kt.giga.home.b2b.mapper.ServiceContractMapper;
import com.kt.giga.home.b2b.repository.BizContractRepository;
import com.kt.giga.home.b2b.repository.ServiceContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * com.kt.giga.home.b2b.core.service
 * <p>
 * Created by cecil on 2017. 1. 23..
 */
@Service
public class ServiceContractService implements IServiceContractService {

    private final BizContractRepository bizContractRepository;

    private final ServiceContractRepository serviceContractRepository;

    private final ServiceContractMapper serviceContractMapper;

    @Autowired
    public ServiceContractService(BizContractRepository bizContractRepository, ServiceContractRepository serviceContractRepository, ServiceContractMapper serviceContractMapper) {
        this.bizContractRepository = bizContractRepository;
        this.serviceContractRepository = serviceContractRepository;
        this.serviceContractMapper = serviceContractMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void insertServiceContracts(PreOpenRstReqInfo serviceContractsWrapper) {

        DeviceModel deviceModel = new DeviceModel();
        deviceModel.setName("MC03-611ZM");
        deviceModel.setCode("K7004292");


        BizContract bizContract = getBizContract(serviceContractsWrapper.getBizcontno());

        List<PreOpenRstLs> openRstLs = serviceContractsWrapper.getPreOpenRstLs();

        checkIfPreOpenRstLsExists(openRstLs);

        serviceContractsWrapper.getPreOpenRstLs().stream().map(preOpenRstLs -> serviceContractMapper.toServiceContract(preOpenRstLs, bizContract, deviceModel)).forEach(serviceContractRepository::save);

        serviceContractRepository.flush();
    }

    private void checkIfPreOpenRstLsExists(List<PreOpenRstLs> openRstLs) {
        Optional<PreOpenRstLs> exist = openRstLs
                .stream()
                .filter(preOpenRstLs -> serviceContractRepository.exists(Long
                        .parseLong(preOpenRstLs.getSvccontno())))
                .findFirst();

        if (exist.isPresent())
            throw new ServiceContractAlreadyExistException("Service Contract [" + exist.get()
                                                                                       .getSvccontno() + "] already exists.");
    }

    private BizContract getBizContract(String bizContractNumber) {
        BizContract bizContract = bizContractRepository.findOne(bizContractNumber);

        if (null == bizContract) {
            throw new BizContractNotFoundException("BizContract [" + bizContractNumber + "] not found.");
        }
        return bizContract;
    }

}
