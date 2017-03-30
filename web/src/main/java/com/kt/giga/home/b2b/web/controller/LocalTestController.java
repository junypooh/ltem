package com.kt.giga.home.b2b.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kt.giga.home.b2b.domain.integration.api.MapUserCtnRequest;
import com.kt.giga.home.b2b.domain.integration.api.ProvisionRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * com.kt.giga.home.b2b.web.controller
 * <p>
 * Created by cecil on 2017. 3. 21..
 */
@Slf4j
@RestController
public class LocalTestController {

    @PostMapping("/manager-api/ghms/ltem/ltemSubscription")
    public GhmsResult provision(ProvisionRequest request) {
        log.debug("ProvisionReqeust : {}", request);
        return new GhmsResult("1", String.valueOf(new Random().nextInt(5000)));
    }

    @PostMapping("/manager-api/ghms/ltem/ltemConfirm")
    public GhmsResult mapUser(MapUserCtnRequest request) {
        log.debug("MapUserCtnRequest : {}", request);
        return new GhmsResult("1");
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static public class GhmsResult {

        public GhmsResult(String resultCode) {
            this.resultCode = resultCode;
        }

        public GhmsResult(String resultCode, String svcTgtSeq) {
            this(resultCode);
            this.svcTgtSeq = svcTgtSeq;
        }

        String resultCode;
        String svcTgtSeq;
    }
}
