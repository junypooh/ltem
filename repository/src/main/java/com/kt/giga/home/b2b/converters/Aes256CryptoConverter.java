package com.kt.giga.home.b2b.converters;

import com.kt.giga.home.b2b.base.SpringContextBridge;
import com.kt.giga.home.b2b.base.SpringContextBridgedServices;
import com.kt.giga.home.b2b.crypto.ICryptoService;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * com.kt.giga.home.b2b.converters
 * <p>
 * Created by cecil on 2017. 1. 20..
 */
@Slf4j
@Converter
public class Aes256CryptoConverter implements AttributeConverter<String, String> {


    @Override
    public String convertToDatabaseColumn(String attribute) {
        ICryptoService cryptoService = getCryptoService();
        return (null == attribute) ? null : cryptoService.encrypt(attribute);
    }

    private ICryptoService getCryptoService() {
        SpringContextBridgedServices bridgedServices = SpringContextBridge.services();

        if (null == bridgedServices) {
            log.error("SpringContextBridgedServices is null.");
            throw new NullPointerException("SpringContextBridgedServices is null.");
        }

        return bridgedServices.getCryptoService();
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
//        log.debug("Data : {}", dbData);
        ICryptoService cryptoService = getCryptoService();
        return (null == dbData) ? null : cryptoService.decrypt(dbData);
    }
}
