package com.kt.giga.home.b2b.core.dms.domain;

import com.kt.giga.home.b2b.validator.Imei;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by 민우 on 2016-12-02.
 */
@Data
public class DeviceCloseRequest {

    @NotNull(message = "imei {validation.error.null}")
    @NotEmpty(message = "imei {validation.error.notEmpty}")
    @Length(min = 15, max = 15, message = "imei {validation.error.length}")
    @Imei(message = "{validation.error.imei}")
    protected String imei;

}
