package com.kt.giga.home.b2b.domain.sms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * com.kt.giga.home.b2b.domain.sms
 * <p>
 * Created by cecil on 2017. 3. 8..
 */
@Data
public class SmsResponse {

    @JsonProperty("transacId")
    private String transactionId;

}
