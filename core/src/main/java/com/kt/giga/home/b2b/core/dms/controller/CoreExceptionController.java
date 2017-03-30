package com.kt.giga.home.b2b.core.dms.controller;

import com.kt.giga.home.b2b.core.dms.domain.DeviceResponse;
import com.kt.giga.home.b2b.core.dms.service.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Created by 민우 on 2016-11-25.
 */
@ControllerAdvice
public class CoreExceptionController {

    @Autowired
    private MessageSource msgSource;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DeviceController.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public DeviceResponse deviceValidationError(MethodArgumentNotValidException ex) {
        BindingResult resultCode = ex.getBindingResult();
        FieldError resultMsg = resultCode.getFieldError();

        LOGGER.error("############# ERROR {}",ex);

        return processFieldError(resultMsg);
    }

    private DeviceResponse processFieldError(FieldError resultMsg) {
        DeviceResponse message = null;
        if (resultMsg != null) {
            Locale currentLocale = LocaleContextHolder.getLocale();
//            String msg = msgSource.getMessage(resultMsg.getDefaultMessage(), null, currentLocale);
            String msg = resultMsg.getDefaultMessage();

            message = new DeviceResponse ("NOK", msg);
        }
        return message;
    }


    @ExceptionHandler(value = {CoreException.class})
    protected ResponseEntity<String> handleAPIException(HttpServletRequest request, CoreException e) {
        return new ResponseEntity<>(e.getJSONErrorMessage(), e.getStatus());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected DeviceResponse handleException(Exception ex) {

        LOGGER.error("############# ERROR {}",ex.getMessage());

        DeviceResponse deviceResponse = new DeviceResponse();
        deviceResponse.setResultCode("NOK");
        deviceResponse.setResultMsg("Exception");
        return new DeviceResponse("NOK", "Exception");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ResponseBody
    public DeviceResponse requestHandlingNoHandlerFound() {
        return new DeviceResponse("404", "404 error code");
    }

}
