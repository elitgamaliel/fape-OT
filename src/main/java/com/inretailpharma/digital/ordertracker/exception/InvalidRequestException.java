package com.inretailpharma.digital.ordertracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends OrderTrackerException {

	private static final long serialVersionUID = 4031192463001762641L;

	public InvalidRequestException(String message) {
		super(message);
	}

}
