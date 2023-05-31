package com.inretailpharma.digital.ordertracker.exception;

import org.springframework.http.HttpStatus;

public class UnknownDeviceException extends OrderTrackerException {
	private static final long serialVersionUID = 5691128617023161078L;

	public UnknownDeviceException(String message, String errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }
}
