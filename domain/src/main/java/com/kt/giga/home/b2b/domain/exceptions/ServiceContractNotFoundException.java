package com.kt.giga.home.b2b.domain.exceptions;

/**
 * com.kt.giga.home.b2b.domain.exceptions
 * <p>
 * Created by cecil on 2017. 2. 15..
 */
public class ServiceContractNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -7729225510108990651L;

    public ServiceContractNotFoundException(String message) {
        super(message);
    }
}
