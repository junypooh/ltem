package com.kt.giga.home.b2b.service;

import com.kt.giga.home.b2b.domain.bssiot.ServiceActivationResponse;
import com.kt.giga.home.b2b.domain.bssiot.ServiceStatusChangeRequest;
import com.kt.giga.home.b2b.domain.bssiot.ServiceStatusChangeResponse;

/**
 * Created by alsdnfl on 2017-03-13.
 */
public interface IBssIotSenderService {

    ServiceActivationResponse serviceActivation(String svcContNo);

    ServiceStatusChangeResponse requestStatusChange(String serviceContractNo, ServiceStatusChangeRequest.Command command);
}
