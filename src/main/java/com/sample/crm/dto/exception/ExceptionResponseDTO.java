package com.sample.crm.dto.exception;

import com.sample.crm.exception.model.ExceptionMessage;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ExceptionResponseDTO {
    private LocalDateTime timestamp;

    private String status;

    private int statusCode;

    private String exceptionMessage;

    private ExceptionMessage exceptionType;

    private String path;
}
