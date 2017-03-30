package com.kt.giga.home.b2b.domain.integration.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.MoreObjects;
import lombok.*;

/**
 * com.kt.giga.home.b2b.domain.integration.api
 * <p>
 * Created by cecil on 2017. 3. 1..
 */
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class MapUserCtnRequest {

    @JsonProperty("svcTgtSeq")
    @NonNull
    private String svcTgtSeq;

    @JsonProperty("telNo")
    @NonNull
    private String telNo;

    @JsonProperty("authNo")
    private String authNo;

    @JsonProperty("confirmCd")
    @NonNull
    private RequestType confirmCd;

    /**
     * com.kt.giga.home.b2b.domain.integration.api
     * <p>
     * Created by cecil on 2017. 3. 1..
     */
    public static enum RequestType {
        REGISTER("I", "등록"),
        UPDATE("U", "수정"),
        DEREGISTER("D", "삭제");

        private String code;
        private String description;

        @JsonValue
        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        RequestType(String code, String description) {
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
