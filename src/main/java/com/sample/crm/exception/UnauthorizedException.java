package com.sample.crm.exception;

import com.sample.crm.exception.model.ExceptionMessage;

public class UnauthorizedException extends CrmRuntimeException {
    public UnauthorizedException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }
}
