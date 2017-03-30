package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.entity.BizContract;
import com.kt.giga.home.b2b.entity.Manager;
import com.kt.giga.home.b2b.service.IMaskingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by DaDa on 2017-02-23.
 */
@Slf4j
public abstract class AccountManagerMapperDecorator implements AccountManagerMapper {

    @Autowired
    private IMaskingService iMaskingService;

    @Autowired
    @Qualifier("delegate")
    private  AccountManagerMapper accountManagerMapper;

    @Override
    public List<B2bManager> toB2bManagerList(List<Manager> managers) {
        if (managers == null) {
            return null;
        }

        List<B2bManager> list = new ArrayList<>();
        managers.forEach(manager -> list.add(this.toB2bManager(manager)));

        return list;
    }

    @Override
    public B2bManager toB2bManager(Manager manager) {
        B2bManager b2bManager = accountManagerMapper.toB2bManager(manager);
        if(StringUtils.isNotBlank(b2bManager.getName()))
            b2bManager.setName(iMaskingService.maskNameOfPerson(b2bManager.getName()));
        if(StringUtils.isNotBlank(b2bManager.getUsername()))
            b2bManager.setUsername(iMaskingService.maskFromEnd(b2bManager.getUsername(), 3));
        if(StringUtils.isNotBlank(b2bManager.getTelephone()))
            b2bManager.setTelephone(iMaskingService.maskTelephone(b2bManager.getTelephone()));
        if(StringUtils.isNotBlank(b2bManager.getEmail()))
            b2bManager.setEmail(iMaskingService.maskEmail(b2bManager.getEmail()));

        Set<BizContract> bizContracts = manager.getBizContracts();
        Optional<BizContract> first = bizContracts.stream().findFirst();
        first.ifPresent(bizContract -> b2bManager.setContractsName(bizContract.getName()));

        if (bizContracts.size() > 1) {
            b2bManager.setContractsName((b2bManager.getContractsName()) == null ? "계약명 없음" : b2bManager.getContractsName() + "외" + (bizContracts.size() - 1) + "건");
        } else if (bizContracts.size() == 0){
            b2bManager.setContractsName("-");
        }

        return b2bManager;
    }
}
