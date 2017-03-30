package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.entity.Manager;
import com.kt.giga.home.b2b.service.IMaskingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DaDa on 2017-02-10.
 */
@Slf4j
public abstract class B2bManagerMaskingMapperDecorator implements B2bManagerMaskingMapper {

    @Autowired
    private IMaskingService iMaskingService;

    @Autowired
    @Qualifier("delegate")
    private B2bManagerMaskingMapper b2bManagerMaskingMapper;

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

        if (manager == null) {
            return null;
        }

        B2bManager b2bManager = b2bManagerMaskingMapper.toB2bManager(manager);

        if(StringUtils.isNotBlank(b2bManager.getName())) {
            b2bManager.setName(iMaskingService.maskNameOfPerson(b2bManager.getName()));
        }
        return b2bManager;
    }
}
