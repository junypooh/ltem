package com.kt.giga.home.b2b.core.service;

import com.kt.giga.home.b2b.domain.bssiot.receiver.BssIotBizContractDto;
import com.kt.giga.home.b2b.domain.bssiot.receiver.ContSttusChgRstReqInfo;

/**
 * com.kt.giga.home.b2b.core.service
 * <p>
 * Created by cecil on 2017. 1. 22..
 */
public interface IBizContractService {
    void insertBizContract(BssIotBizContractDto bssIotBizContractDto);

    void changeBizContract(ContSttusChgRstReqInfo contSttusChgRstReqInfo);
}
