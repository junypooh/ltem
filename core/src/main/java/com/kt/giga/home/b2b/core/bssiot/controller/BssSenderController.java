package com.kt.giga.home.b2b.core.bssiot.controller;


import com.kt.giga.home.b2b.domain.bssiot.ServiceActivationResponse;
import com.kt.giga.home.b2b.domain.bssiot.ServiceStatusChangeRequest;
import com.kt.giga.home.b2b.domain.bssiot.ServiceStatusChangeResponse;
import com.kt.giga.home.b2b.service.IBssIotSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by alsdnfl on 2017-02-06.
 */
@Slf4j
@RestController
@RequestMapping(value = "/sender")
public class BssSenderController {

    @Autowired
    IBssIotSenderService bssIotSenderService;

    @GetMapping(value = "/svcactva")
    public ServiceActivationResponse  svcactva(String svcContNo){


        //String svcContNo ="980113558";
      //  String svcContNo =serviceActivationRequest.getSvcContSerial();
        log.info("Bss-Iot Sender request : {}", svcContNo);

        ServiceActivationResponse result = bssIotSenderService.serviceActivation(svcContNo);

        log.info("Bss-Iot Sender response : {}", result);

        return result;
    }

    @GetMapping(value = "/contsttuschg/SUS")
    public ServiceStatusChangeResponse contsttuschgSUS(String svcContNo){

        ServiceStatusChangeResponse result = bssIotSenderService.requestStatusChange(svcContNo, ServiceStatusChangeRequest.Command.SUSPEND);


        log.info("Bss-Iot Sender response : {}", result);

        return result;
    }

    @GetMapping(value = "/contsttuschg/RSP")
    public ServiceStatusChangeResponse contsttuschgRSP(String svcContNo){

        ServiceStatusChangeResponse result = bssIotSenderService.requestStatusChange(svcContNo, ServiceStatusChangeRequest.Command.RESUME);

        log.info("Bss-Iot Sender response : {}", result);

        return result;
    }

}
