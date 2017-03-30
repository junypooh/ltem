package com.kt.giga.home.b2b.core.dms.controller;


import com.kt.giga.home.b2b.core.dms.domain.DeviceCloseRequest;
import com.kt.giga.home.b2b.core.dms.domain.DeviceOpenRequest;
import com.kt.giga.home.b2b.core.dms.domain.DeviceResponse;
import com.kt.giga.home.b2b.core.dms.service.IDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by 민우 on 2016-11-24.
 */
@Slf4j(topic = "com.kt.giga.home.b2b.core.dms")
@RestController
@RequestMapping(value = "/dms")
public class DeviceController {

    @Autowired
    private IDeviceService deviceService;

    @PostMapping(value = "/devices")
    public DeviceResponse openDevice(@RequestBody @Valid DeviceOpenRequest deviceOpenRequest) {

        log.info("Received from DMS : {} - {}", deviceOpenRequest.getClass().getName(), deviceOpenRequest);

        DeviceResponse deviceResponse = deviceService.openDevice(deviceOpenRequest);

        log.info("Response to DMS : {}", deviceResponse);

        return deviceResponse;

    }

    @DeleteMapping(value = "/devices")
    public DeviceResponse deleteDevice(@RequestBody @Valid DeviceCloseRequest deviceCloseRequest) {

        log.info("Received from DMS : {} : {}", deviceCloseRequest.getClass().getName(), deviceCloseRequest);

        DeviceResponse deviceResponse = deviceService.closeDevice(deviceCloseRequest);

        log.info("Response to DMS : {}", deviceResponse);

        return deviceResponse;
    }

}
