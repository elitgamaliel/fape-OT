package com.inretailpharma.digital.ordertracker.rest.core;

import com.inretailpharma.digital.ordertracker.dto.ErrorDto;
import com.inretailpharma.digital.ordertracker.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class UserDefinedExceptionHandler {

    @ExceptionHandler(UnknownDeviceException.class)
    protected ResponseEntity<ErrorDto> handleUnknownDevice(UnknownDeviceException exception, HttpServletRequest request) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ErrorDto(exception, request.getRequestURI()), exception.getStatus());
    }

    @ExceptionHandler(UserException.class)
    protected ResponseEntity<ErrorDto> handleUser(UserException exception, HttpServletRequest request) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ErrorDto(exception, request.getRequestURI()), exception.getStatus());
    }

    @ExceptionHandler(OrderException.class)
    protected ResponseEntity<ErrorDto> handleOrder(OrderException exception, HttpServletRequest request) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ErrorDto(exception, request.getRequestURI()), exception.getStatus());
    }

    @ExceptionHandler(ExternalException.class)
    protected ResponseEntity<ErrorDto> handleExternal(ExternalException exception, HttpServletRequest request) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ErrorDto(exception, request.getRequestURI()), exception.getStatus());
    }

    @ExceptionHandler(OrderTrackerException.class)
    protected ResponseEntity<ErrorDto> handleOrderTracker(OrderTrackerException exception, HttpServletRequest request) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ErrorDto(exception, request.getRequestURI()), exception.getStatus());
    }

    @ExceptionHandler(NetworkException.class)
    protected ResponseEntity<ErrorDto> handleNetwork(NetworkException exception, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorDto(exception, request.getRequestURI()), exception.getStatus());
    }

    @ExceptionHandler(InvalidRequestException.class)
    protected ResponseEntity<ErrorDto> handleNetwork(InvalidRequestException exception, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorDto(exception, request.getRequestURI()), exception.getStatus());
    }
}
