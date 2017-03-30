package com.kt.giga.home.b2b.domain.integration.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserMapResult {

    @JsonProperty("svcTgtSeq")
    private String serviceTargetSequence;

}
