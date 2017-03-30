package com.kt.giga.home.b2b.core.api.controller;

import com.kt.giga.home.b2b.core.dms.domain.DeviceResponse;
import com.kt.giga.home.b2b.core.service.IUserMapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by alsdnfl on 2017-03-03.
 */
@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserMapController {

    @Autowired
    IUserMapService userMapService;

    @PutMapping(value = "/map/{svcTgtSeq}")
    public DeviceResponse userMap(@PathVariable("svcTgtSeq") Long svcTgrSeq ) {
        DeviceResponse deviceResponse = new DeviceResponse();
        log.info("UserMapController.userMap manager request : {} ", svcTgrSeq);

        deviceResponse = userMapService.userMap(svcTgrSeq) ;

        log.info("UserMapController.userMap manager response : {} ", deviceResponse);

        return deviceResponse;
    }
}
