package com.inretailpharma.digital.ordertracker.exception;

import java.security.NoSuchAlgorithmException;

public class HashException extends RuntimeException {

	private static final long serialVersionUID = 8458578577308784042L;

	public HashException (final String message, final NoSuchAlgorithmException ex) {
		super(message, ex);
	}

}
