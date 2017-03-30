package com.kt.giga.home.b2b.core.service;

import com.kt.giga.home.b2b.domain.CapacityUnit;
import com.kt.giga.home.b2b.domain.DeviceStatus;
import com.kt.giga.home.b2b.domain.bssiot.receiver.BssIotBizContractDto;
import com.kt.giga.home.b2b.domain.bssiot.receiver.ContSttusChgRstReqInfo;
import com.kt.giga.home.b2b.domain.bssiot.receiver.UseQntRstLs;
import com.kt.giga.home.b2b.domain.bssiot.receiver.UseQntRstReqInfo;
import com.kt.giga.home.b2b.domain.exceptions.*;
import com.kt.giga.home.b2b.entity.*;
import com.kt.giga.home.b2b.mapper.BizContractMapper;
import com.kt.giga.home.b2b.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * com.kt.giga.home.b2b.core.service
 * <p>
 * Created by cecil on 2017. 1. 22..
 */
@Slf4j
@Service
public class BizContractService implements IBizContractService {

    private final BizContractRepository bizContractRepository;

    private final CompanyRepository companyRepository;

    private final BizContractMapper bizContractMapper;

    private final ServiceContractRepository serviceContractRepository;

    private final BizContUsageRepository bizContUsageRepository;

    private final SvcContUsageRepository svcContUsageRepository;

    @Autowired
    public BizContractService(BizContractRepository bizContractRepository, CompanyRepository companyRepository, BizContractMapper bizContractMapper, ServiceContractRepository serviceContractRepository, BizContUsageRepository bizContUsageRepository, SvcContUsageRepository svcContUsageRepository) {
        this.bizContractRepository = bizContractRepository;
        this.companyRepository = companyRepository;
        this.bizContractMapper = bizContractMapper;
        this.serviceContractRepository = serviceContractRepository;
        this.bizContUsageRepository = bizContUsageRepository;
        this.svcContUsageRepository = svcContUsageRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void insertBizContract(BssIotBizContractDto bssIotBizContractDto) {

        Company company = companyRepository.findOne(bssIotBizContractDto.getCmpncd());

        if (null == company) {
            company = new Company();
            company.setCode(bssIotBizContractDto.getCmpncd());
            company = companyRepository.save(company);
        }


        if (null != bizContractRepository.findOne(bssIotBizContractDto.getBizcontno())) {
            throw new BizContractAlreadyExistException("BIZ_CONT_NO [" + bssIotBizContractDto.getBizcontno() + "] already exists.");
        }

        BizContract bizContract = bizContractMapper.toBizContract(bssIotBizContractDto);

        bizContract.setCompany(company);
        bizContractRepository.saveAndFlush(bizContract);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void changeBizContract(ContSttusChgRstReqInfo contSttusChgRstReqInfo) {

        BizContract bizContract = bizContractRepository.findOne(contSttusChgRstReqInfo.getBizcontno());

        if (null == bizContract) {
            throw new BizContractNotFoundException("BizContract [" + contSttusChgRstReqInfo.getBizcontno() + "] not found.");
        }

        if (contSttusChgRstReqInfo.getBizcontno().equals(contSttusChgRstReqInfo.getTgtbizcontno())){
            throw new BizContractAlreadyExistException("Equal ["+contSttusChgRstReqInfo.getBizcontno()+" = "+contSttusChgRstReqInfo.getTgtbizcontno() +"]");
        }

        if (contSttusChgRstReqInfo.getBizcontchgcd().equals("CAN")) {
            expireBizContract(contSttusChgRstReqInfo, bizContract);
        } else {
            extendBizContract(contSttusChgRstReqInfo, bizContract);
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void insertUsage(UseQntRstReqInfo useQntRstReqInfo) {
        BizContract bizContract = bizContractRepository.findOne(useQntRstReqInfo.getBizcontno());

        if (null == bizContract) {
            throw new BizContractNotFoundException("BizContract [" + useQntRstReqInfo.getBizcontno() + "] not found.");
        }

        Optional<ServiceContract> wrongServiceContract = bizContract.getCurrentServiceContracts()
                                                                    .stream()
                                                                    .filter(serviceContract -> serviceContractRepository
                                                                            .findOne(serviceContract
                                                                                    .getSerialNumber()) == null || !serviceContract
                                                                            .getCurrentBizContract()
                                                                            .equals(bizContract))
                                                                    .findFirst();

        if (wrongServiceContract.isPresent()) {
            throw new ServiceContractNotFoundException("Service contract [" + wrongServiceContract.get()
                                                                                                  .getSerialNumber() + "] may doesn't exist or doesn't included by BizContract [" + bizContract
                    .getSerialNumber() + "]");
        }

        insertUsage(bizContract, useQntRstReqInfo);

    }

    private void insertUsage(BizContract bizContract, UseQntRstReqInfo useQntRstReqInfo) {
        LocalDate previous = LocalDate.now().minusMonths(1);

        throwIfBizContUsageExists(bizContract, previous);

        Optional<UseQntRstLs> svcUsageExists = useQntRstReqInfo.getUseQntRstLs().stream().filter(useQntRstLs ->
                svcContUsageRepository.findOne(new SvcContUsage.SvcContUsagePK(Long.parseLong(useQntRstLs
                        .getSvccontno()), (short) previous
                        .getYear(), (short) previous.getMonth().getValue()))
                        != null).findFirst();

        if (svcUsageExists.isPresent())
            throw new ServiceContractAlreadyExistException("Service contract usage [" + svcUsageExists.get() + "] already exists.");

        BizContUsage bizContUsage = new BizContUsage();
        bizContUsage.setSerialNumber(useQntRstReqInfo.getBizcontno());
        bizContUsage.setBizContract(bizContract);
        bizContUsage.setCapacity(Long.valueOf(useQntRstReqInfo.getBizalwdqnt()));
        bizContUsage.setCapacityUnit(CapacityUnit.getCapacityUnit(useQntRstReqInfo.getBizalwdunitcd().charAt(0)));
        bizContUsage.setUsage(Long.valueOf(useQntRstReqInfo.getSvcconttotalwdqnt()));
        bizContUsage.setYear((short) previous.getYear());
        bizContUsage.setMonth((short) previous.getMonth().getValue());

        bizContUsageRepository.saveAndFlush(bizContUsage);

        useQntRstReqInfo.getUseQntRstLs().forEach(useQntRstLs -> {
            SvcContUsage svcContUsage = new SvcContUsage();
            svcContUsage.setServiceContract(serviceContractRepository.findOne(Long.valueOf(useQntRstLs.getSvccontno())));
            svcContUsage.setSerialNumber(Long.valueOf(useQntRstLs.getSvccontno()));
            svcContUsage.setYear((short) previous.getYear());
            svcContUsage.setMonth((short) previous.getMonth().getValue());
            svcContUsage.setUsage(Long.valueOf(useQntRstLs.getChguse()));
            svcContUsage.setUsageUnitCd(CapacityUnit.PACKET);

            svcContUsageRepository.save(svcContUsage);
        });

        svcContUsageRepository.flush();


    }

    private void throwIfBizContUsageExists(BizContract bizContract, LocalDate previous) {
        BizContUsage bizContUsageExist = bizContUsageRepository.findOne(new BizContUsage.BizContUsagePK(bizContract.getSerialNumber(), (short) previous
                .getYear(), (short) previous
                .getMonth()
                .getValue()));

        if (bizContUsageExist != null) {
            throw new BizContractUsageAlreadyExistException("BizContUsage [" + bizContUsageExist + "] already exists.");
        }
    }

    private void extendBizContract(ContSttusChgRstReqInfo contSttusChgRstReqInfo, BizContract bizContract) {
        String newBizContractNo = contSttusChgRstReqInfo.getTgtbizcontno();

        BizContract newBizContract = bizContractRepository.findOne(newBizContractNo);

        if (null == newBizContract)
            throw new BizContractNotFoundException("New Biz Contract [" + newBizContractNo + "] not found.");

        List<Long> serviceContractNos = contSttusChgRstReqInfo.getContSttusChgRstLs()
                                                              .stream()
                                                              .map(contSttusChgRstLs -> Long.parseLong(contSttusChgRstLs
                                                                      .getSvccontno()))
                                                              .collect(Collectors.toList());

        List<ServiceContract> serviceContracts = serviceContractRepository.findBySerialNumbersAndCurrentBizContract(serviceContractNos, bizContract);

        serviceContracts.forEach(serviceContract -> {
            serviceContract.setCurrentBizContract(newBizContract);
            serviceContractRepository.save(serviceContract);
        });

        serviceContractRepository.flush();

    }

    private void expireBizContract(ContSttusChgRstReqInfo contSttusChgRstReqInfo, BizContract bizContract) {
        bizContract.setEndDate(LocalDate.now());
        bizContractRepository.saveAndFlush(bizContract);

        Long countOfServiceContracts = serviceContractRepository.countBySerialNumbersAndCurrentBizContract(contSttusChgRstReqInfo
                .getContSttusChgRstLs()
                .stream()
                .map(contSttusChgRstLs -> Long
                        .parseLong(contSttusChgRstLs
                                .getSvccontno()))
                .collect(Collectors
                        .toList()), bizContract);

        if (countOfServiceContracts != contSttusChgRstReqInfo.getContSttusChgRstLs().size())
            throw new RuntimeException("Count of service contracts doesn't match.");

        contSttusChgRstReqInfo.getContSttusChgRstLs().forEach(contSttusChgRstLs -> {
            ServiceContract contract = serviceContractRepository.findOne(Long
                    .parseLong(contSttusChgRstLs
                            .getSvccontno()));
            contract.setDeviceStatus(DeviceStatus.EXPIRATION_RECEIVED);
            serviceContractRepository.save(contract);
        });

        serviceContractRepository.flush();
    }
}
