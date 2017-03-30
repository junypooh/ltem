package com.kt.giga.home.b2b.domain.exceptions;

/**
 * com.kt.giga.home.b2b.domain.exceptions
 * <p>
 * Created by cecil on 2017. 1. 23..
 */
public class BizContractAlreadyExistException extends RuntimeException {
    private static final long serialVersionUID = 3849670321536721393L;

    public BizContractAlreadyExistException(String message) {
        super(message);
    }
}
