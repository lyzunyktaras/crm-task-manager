package com.sample.crm.configuration;

import javax.naming.AuthenticationException;

import com.sample.crm.dto.exception.ExceptionResponseDTO;
import com.sample.crm.exception.NotFoundException;
import com.sample.crm.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleNotFoundException(
            NotFoundException notFoundException, ServletWebRequest servletWebRequest) {
        ExceptionResponseDTO exceptionResponse =
                ExceptionResponseDTO.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .exceptionMessage(notFoundException.getMessage())
                        .path(servletWebRequest.getRequest().getRequestURI())
                        .build();

        log.error(notFoundException.getMessage(), notFoundException);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponseDTO> handleAuthenticationException(
        AuthenticationException authenticationException, ServletWebRequest servletWebRequest) {

        ExceptionResponseDTO exceptionResponse =
                ExceptionResponseDTO.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .exceptionMessage(authenticationException.getMessage())
                        .path(servletWebRequest.getRequest().getRequestURI())
                        .build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDTO> handleNotFoundException(
            Exception exception, ServletWebRequest servletWebRequest) {
        ExceptionResponseDTO exceptionResponse =
                ExceptionResponseDTO.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .exceptionMessage(exception.getMessage())
                        .path(servletWebRequest.getRequest().getRequestURI())
                        .build();

        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
