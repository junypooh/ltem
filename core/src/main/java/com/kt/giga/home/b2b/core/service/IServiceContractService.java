package com.kt.giga.home.b2b.core.service;

import com.kt.giga.home.b2b.domain.bssiot.receiver.PreOpenRstReqInfo;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * com.kt.giga.home.b2b.core.service
 * <p>
 * Created by cecil on 2017. 1. 23..
 */
public interface IServiceContractService {
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    void insertServiceContracts(PreOpenRstReqInfo serviceContractsWrapper);
}
