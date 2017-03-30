package com.kt.giga.home.b2b.domain.exceptions;

/**
 * com.kt.giga.home.b2b.domain.exceptions
 * <p>
 * Created by cecil on 2017. 2. 15..
 */
public class ServiceContractAlreadyExistException extends RuntimeException {
    private static final long serialVersionUID = -7235784828969575850L;

    public ServiceContractAlreadyExistException(String message) {
        super(message);
    }
}
