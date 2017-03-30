package com.kt.giga.home.b2b.base;

import com.kt.giga.home.b2b.crypto.ICryptoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * com.kt.giga.home.b2b.base
 * <p>
 * Created by cecil on 2017. 1. 20..
 */
@Slf4j
@Component
public class SpringContextBridge implements SpringContextBridgedServices, ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private ICryptoService cryptoService;

    @Autowired
    public SpringContextBridge(ICryptoService cryptoService) {
        log.debug("Initializing SpringContextBridge...");
        this.cryptoService = cryptoService;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextBridge.applicationContext = applicationContext;
    }

    @Override
    public ICryptoService getCryptoService() {
        return cryptoService;
    }

    public static SpringContextBridgedServices services() {
        return (null == applicationContext) ? null : applicationContext.getBean(SpringContextBridgedServices.class);
    }
}
