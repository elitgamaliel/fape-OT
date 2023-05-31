package com.inretailpharma.digital.ordertracker.exception;

import org.springframework.http.HttpStatus;

import com.inretailpharma.digital.ordertracker.utils.Constant;

public class OrderFinishedException extends OrderTrackerException {

	private static final long serialVersionUID = -8989375900064820972L;

	public OrderFinishedException(final String message) {
        this(message, Constant.ErrorCode.DEFAULT, HttpStatus.INTERNAL_SERVER_ERROR);
    }

	public OrderFinishedException(String message, String errorCode) {
        this(message, errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

	public OrderFinishedException(String message, String errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }
}