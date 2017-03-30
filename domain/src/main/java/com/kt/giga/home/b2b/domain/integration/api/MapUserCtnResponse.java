package com.kt.giga.home.b2b.domain.integration.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.MoreObjects;
import lombok.Data;
import lombok.NonNull;

/**
 * com.kt.giga.home.b2b.service.api
 * <p>
 * Created by cecil on 2017. 3. 1..
 */
@Data
public class MapUserCtnResponse {

    @NonNull
    @JsonProperty("resultCode")
    private Result result;

    /**
     * com.kt.giga.home.b2b.domain.integration.api
     * <p>
     * Created by cecil on 2017. 3. 1..
     */
    public static enum Result {

        SUCCESS("1", "성공"),
        FAILURE("2", "실패");

        private String code;
        private String description;

        @JsonValue
        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        Result(String code, String description) {
            this.code = code;
            this.description = description;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                              .add("name", name())
                              .add("code", code)
                              .add("description", description)
                              .toString();
        }
    }
}
