package com.kt.giga.home.b2b.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * com.kt.giga.home.b2b.domain
 * <p>
 * Created by cecil on 2017. 3. 21..
 */
@Builder
@Data
public class UserInformation {
    String  devicePlace;
    String  userName;
    String  userCtn;
    Boolean status;

    public UserInformation() {

    }

    public UserInformation(String devicePlace, String userName, String userCtn, boolean status) {
        this.devicePlace = devicePlace;
        this.userName = userName;
        this.userCtn = userCtn;
        this.status = status;
    }

    public UserInformation(String devicePlace, String userName, String userCtn, String status) {
        this(devicePlace, userName, userCtn, StringUtils.defaultString(status.toUpperCase(), "N").equals("Y"));

    }


    public void setStatus(boolean status) {
        this.status = status;
    }

    @JsonProperty("status")
    public void setStatus(String bool) {
        this.status = StringUtils.defaultString(bool.toUpperCase(), "N").equals("Y");
    }
}
