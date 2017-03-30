package com.kt.giga.home.b2b.core.api.controller;

import com.kt.giga.home.b2b.domain.integration.api.UserMapResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * com.kt.giga.home.b2b.core.api.controller
 * <p>
 * Created by cecil on 2017. 3. 2..
 */
@Slf4j
@RestController(value = "/api")
public class ApiController {

    @PostMapping("/mapResult")
    public void mapResult(UserMapResult userMapResult) {
        log.debug("User Mapping Result : {}", userMapResult);
    }
}
