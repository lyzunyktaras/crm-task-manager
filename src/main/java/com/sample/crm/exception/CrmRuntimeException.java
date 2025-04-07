package com.sample.crm.exception;

import com.sample.crm.exception.model.ExceptionMessage;
import lombok.Getter;

@Getter
public class CrmRuntimeException extends RuntimeException {
    public CrmRuntimeException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.toString());
    }
}
