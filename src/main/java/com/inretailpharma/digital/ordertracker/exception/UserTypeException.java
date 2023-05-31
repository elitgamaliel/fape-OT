package com.inretailpharma.digital.ordertracker.exception;

import org.springframework.http.HttpStatus;

import com.inretailpharma.digital.ordertracker.utils.Constant;

public class UserTypeException extends OrderTrackerException {

	private static final long serialVersionUID = -6180862931844504399L;

	public UserTypeException(final String message) {
        this(message, Constant.ErrorCode.DEFAULT, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	public UserTypeException(String message, String errorCode) {
        this(message, errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	public UserTypeException(String message, String errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }
}
