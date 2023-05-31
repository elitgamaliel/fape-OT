package com.inretailpharma.digital.ordertracker.exception;

import org.springframework.http.HttpStatus;

import com.inretailpharma.digital.ordertracker.utils.Constant;


public class OrderTrackerException extends RuntimeException {

	private static final long serialVersionUID = 609330157685593231L;
	private final HttpStatus status;
	private final String errorCode;

    public OrderTrackerException(final String message) {
        this(message, Constant.ErrorCode.DEFAULT, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public OrderTrackerException(final String message, String errorCode, final HttpStatus status) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

	public String getErrorCode() {
		return errorCode;
	}
    
}
