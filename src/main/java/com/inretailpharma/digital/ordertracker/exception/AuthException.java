package com.inretailpharma.digital.ordertracker.exception;

public class AuthException extends RuntimeException {
	
	private static final long serialVersionUID = -8795696950218919634L;

	public AuthException (final String message) {
		super(message);
	}
	
	public AuthException (final Throwable ex) {
		super(ex);
	}

}
