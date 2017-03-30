package com.kt.giga.home.b2b.domain.exceptions;

/**
 * com.kt.giga.home.b2b.domain.exceptions
 * <p>
 * Created by cecil on 2017. 1. 23..
 */
public class BizContractNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 7343864456123089240L;

    public BizContractNotFoundException(String message) {
        super(message);
    }
}
