package com.kt.giga.home.b2b.core.dms.controller;

import com.kt.giga.home.b2b.core.dms.domain.DeviceResponse;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 민우 on 2016-12-30.
 */
@RestController
public class IndexController implements ErrorController{

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public DeviceResponse error(){
        return new DeviceResponse("NOK","page not found");
    }

    @Override
    public String getErrorPath(){
        return PATH;
    }
}
