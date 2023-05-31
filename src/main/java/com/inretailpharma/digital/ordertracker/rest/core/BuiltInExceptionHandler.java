package com.inretailpharma.digital.ordertracker.rest.core;

import com.inretailpharma.digital.ordertracker.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.id.IdentifierGenerationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class BuiltInExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<ErrorDto> badRequestStatus(EntityNotFoundException exception, HttpServletRequest request) {
        log.error(exception.getMessage(), exception);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception, request.getRequestURI());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, IllegalArgumentException.class, IdentifierGenerationException.class})
    protected ResponseEntity<ErrorDto> badRequestStatus(Exception exception, HttpServletRequest request) {
        log.error(exception.getMessage(), exception);
        return buildResponseEntity(HttpStatus.BAD_REQUEST, exception, request.getRequestURI());
    }

    @ExceptionHandler({NoSuchElementException.class, UsernameNotFoundException.class})
    protected ResponseEntity<ErrorDto> notFoundStatus(Exception exception, HttpServletRequest request) {
        log.error(exception.getMessage(), exception);
        return buildResponseEntity(HttpStatus.NOT_FOUND, exception, request.getRequestURI());
    }

    private ResponseEntity<ErrorDto> buildResponseEntity(HttpStatus status, Exception exception, String path) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ErrorDto(status, exception, path), status);
    }

}
