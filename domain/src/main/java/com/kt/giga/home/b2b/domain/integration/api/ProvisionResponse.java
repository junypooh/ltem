package com.kt.giga.home.b2b.domain.integration.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.MoreObjects;
import lombok.Data;
import lombok.NonNull;

/**
 * com.kt.giga.home.b2b.domain
 * <p>
 * Created by cecil on 2017. 3. 1..
 */
@Data
public class ProvisionResponse {

    @NonNull
    @JsonProperty("resultCode")
    Result result;

    @JsonProperty("svcTgtSeq")
    String serviceTargetSequence;

    /**
     * com.kt.giga.home.b2b.domain.integration.api
     * <p>
     * Created by cecil on 2017. 3. 1..
     */
    public static enum Result {
        SUCCESS("1"),
        FAILURE("2");

        private String result;

        @JsonValue
        public String getResult() {
            return result;
        }

        Result(String result) {
            this.result = result;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                              .add("name", name())
                              .add("result", result)
                              .toString();
        }
    }
}
