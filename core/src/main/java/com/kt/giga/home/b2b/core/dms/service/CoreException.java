package com.kt.giga.home.b2b.core.dms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

/**
 * Created by 민우 on 2016-11-25.
 */
public class CoreException extends Exception{
//public class CoreException {
    @Setter @Getter
    private HttpStatus status;
    private String errorCode;
    private String detailedErrorCode;
    private String msg;

    public CoreException(String msg, HttpStatus status)	{
        super(msg);
        this.status = status;
    }

    public CoreException(String msg, HttpStatus status, String errorCode)	{
        super(msg);
        this.status = status;
        this.errorCode = errorCode;
    }

    public CoreException(String msg, HttpStatus status, String errorCode, String detailedErrorCode) {
        super(msg);
        this.status = status;
        this.errorCode = errorCode;
        this.detailedErrorCode = detailedErrorCode;
    }

    public String getJSONErrorMessage()	{

        HashMap<String, String> msg    = new HashMap<>();
        ObjectMapper            mapper = new ObjectMapper();

        msg.put("code", String.valueOf(getStatus().value()));
        msg.put("msg", getMessage());

        if(StringUtils.isNoneBlank(errorCode)) {
            msg.put("errorCode", errorCode);
        }

        if(StringUtils.isNoneBlank(detailedErrorCode)) {
            msg.put("detailedErrorCode", detailedErrorCode);
        }

        String errMsg = "";
        try {
            errMsg = mapper.writeValueAsString(msg);
        } catch(Exception e) {

        }

        return errMsg;
    }
}
