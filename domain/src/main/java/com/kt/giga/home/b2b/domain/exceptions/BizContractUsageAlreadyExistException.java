package com.kt.giga.home.b2b.domain.exceptions;

/**
 * com.kt.giga.home.b2b.domain.exceptions
 * <p>
 * Created by cecil on 2017. 2. 15..
 */
public class BizContractUsageAlreadyExistException extends RuntimeException {
    private static final long serialVersionUID = 5418157396503337008L;

    public BizContractUsageAlreadyExistException(String message) {
        super(message);
    }
}
