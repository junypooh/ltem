package com.kt.giga.home.b2b.domain.integration.api;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.MoreObjects;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Data
public class ProvisionRequest {

    @NonNull
    private String saId;
    private String iccId;
    private String imei;
    private String ctn;
    @NonNull
    private String secretKey;
    @NonNull
    private String deviceModelName;
    @NonNull
    private String deviceModelCode;
    @NonNull
    private GwCode gwCode;

    public ProvisionRequest(String saId, String iccId, String imei, String ctn, String secretKey, String deviceModelName, String deviceModelCode, GwCode gwCode) {

        this.saId = saId;
        this.iccId = iccId;
        this.imei = imei;
        this.ctn = ctn;
        this.secretKey = secretKey;
        this.deviceModelName = deviceModelName;
        this.deviceModelCode = deviceModelCode;
        this.gwCode = gwCode;
    }


    /**
     * com.kt.giga.home.b2b.domain.integration.api
     * <p>
     * Created by cecil on 2017. 3. 1..
     */
    public static enum GwCode {
        CREATE("M011", "신규등록"),
        SUSPEND("M020", "이용중지"),
        RESUME("M030", "이용부활"),
        CLOSE("M050", "해지");

        private String code;
        private String description;

        GwCode(String code, String description) {
            this.code = code;
            this.description = description;
        }

        @JsonValue
        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
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
