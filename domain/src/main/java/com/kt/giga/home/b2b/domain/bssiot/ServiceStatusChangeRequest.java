package com.kt.giga.home.b2b.domain.bssiot;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by alsdnfl on 2017-03-14.
 */

@AllArgsConstructor
@Data
public class ServiceStatusChangeRequest {

    private String  svcContNo;
    private Command command;

    public enum Command {
        CLOSE("CAN"),
        REOPEN("RCL"),
        SUSPEND("SUS"),
        RESUME("RSP");

        private String code;

        Command(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }
    }
}
