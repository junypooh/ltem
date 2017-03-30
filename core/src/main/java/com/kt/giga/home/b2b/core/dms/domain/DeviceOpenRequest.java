package com.kt.giga.home.b2b.core.dms.domain;

import com.kt.giga.home.b2b.validator.Imei;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by 민우 on 2016-11-24.
 */

@Data
public class DeviceOpenRequest {

    @NotNull(message = "imei {validation.error.null}")
    @NotEmpty(message = "imei {validation.error.notEmpty}")
    @Length(min = 15, max = 15, message = "imei {validation.error.length}")
    @Imei(message = "{validation.error.imei}")
    protected String imei;

    @NotNull(message = "ctn {validation.error.null}")
    @NotEmpty(message = "ctn {validation.error.notEmpty}")
    @Length(min = 10, max = 11, message = "ctn {validation.error.length}")
    protected String ctn;

    @NotNull(message = "secretKey {validation.error.null}")
    @NotEmpty(message = "secretKey {validation.error.notEmpty}")
    @Length(min = 1, max = 64, message = "secretKey {validation.error.length}")
    protected String secretKey;


    @NotNull(message = "modelName {validation.error.null}")
    @NotEmpty(message = "modelName {validation.error.notEmpty}")
    @Length(min = 1, max = 50, message = "modelName {validation.error.length}")
    protected String modelName;

}
