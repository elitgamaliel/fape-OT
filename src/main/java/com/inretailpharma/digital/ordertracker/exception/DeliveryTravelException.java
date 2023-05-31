package com.inretailpharma.digital.ordertracker.exception;

import org.springframework.http.HttpStatus;

import com.inretailpharma.digital.ordertracker.utils.Constant;

public class DeliveryTravelException extends OrderTrackerException {
	private static final long serialVersionUID = 741127700114043382L;

	public DeliveryTravelException(final String message) {
        this(message, Constant.ErrorCode.DEFAULT, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	public DeliveryTravelException(String message, String errorCode) {
        this(message, errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	public DeliveryTravelException(String message, String errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }
}
