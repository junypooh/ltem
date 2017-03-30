package com.kt.giga.home.b2b.domain.integration.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

@Data
public class UnmapUserCtnRequest {

    @JsonProperty("svcTgtSeq")
    @NonNull
    private final String serviceTargetSequence;

}
