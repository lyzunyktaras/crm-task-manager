package com.sample.crm.exception;

import com.sample.crm.exception.model.ExceptionMessage;
import lombok.Getter;

@Getter
public class NotFoundException extends CrmRuntimeException {
    public NotFoundException(ExceptionMessage type) {
        super(type);
    }
}
