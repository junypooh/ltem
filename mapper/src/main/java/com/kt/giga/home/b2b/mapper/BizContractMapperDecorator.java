package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.ContractInfo;
import com.kt.giga.home.b2b.entity.BizContract;
import com.kt.giga.home.b2b.service.IMaskingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by DaDa on 2017-02-20.
 */
@Slf4j
public abstract class BizContractMapperDecorator implements BizContractMapper {

    @Autowired
    private IMaskingService iMaskingService;

    @Autowired
    @Qualifier("delegate")
    private BizContractMapper bizContractMapper;


    @Override
    public List<ContractInfo> toContractInfoList(List<BizContract> bizContracts) {
        if(bizContracts == null) {
            return null;
        }
        List<ContractInfo> list = new ArrayList<>();
        bizContracts.forEach(bizContract -> list.add(this.toContractInfo(bizContract)));
        return list;
    }

    @Override
    public ContractInfo toContractInfo(BizContract bizContract) {
        if(bizContract == null) {
            return null;
        }
        ContractInfo contractInfo = bizContractMapper.toContractInfo(bizContract);
        Set<B2bManager> managers = contractInfo.getManagers();
        managers.forEach(b2bManager -> b2bManager.setName(iMaskingService.maskNameOfPerson(b2bManager.getName())));
        return contractInfo;
    }

}
