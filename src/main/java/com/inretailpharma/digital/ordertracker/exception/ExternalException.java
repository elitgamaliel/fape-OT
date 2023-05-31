package com.inretailpharma.digital.ordertracker.exception;

import org.springframework.http.HttpStatus;

import com.inretailpharma.digital.ordertracker.utils.Constant;

public class ExternalException extends OrderTrackerException {
	private static final long serialVersionUID = 5691128617023161078L;

	public ExternalException(final String message) {
        this(message, Constant.ErrorCode.DEFAULT, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	public ExternalException(String message, String errorCode) {
        this(message, errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	public ExternalException(String message, String errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }
}
