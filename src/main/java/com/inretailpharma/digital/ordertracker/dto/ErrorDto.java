package com.inretailpharma.digital.ordertracker.dto;

import org.springframework.http.HttpStatus;

import com.inretailpharma.digital.ordertracker.exception.OrderTrackerException;
import com.inretailpharma.digital.ordertracker.utils.Constant;

import lombok.Data;

@Data
public class ErrorDto {

    private int status;
    private String error;
    private String exception;
    private String message;
    private String path;
    private String code;
    
    public ErrorDto() {
    }

	public ErrorDto(HttpStatus status, Exception exception, String path) {
		this.status = status.value();
		this.error = status.getReasonPhrase();
		this.exception = exception.getClass().getCanonicalName();
		this.message = exception.getMessage();
		this.path = path;
		this.code = Constant.ErrorCode.DEFAULT;
	}
	
	public ErrorDto(OrderTrackerException exception, String path) {
		this.status = exception.getStatus().value();
		this.error = exception.getStatus().getReasonPhrase();
		this.exception = exception.getClass().getCanonicalName();
		this.message = exception.getMessage();
		this.path = path;
		this.code = exception.getErrorCode();
	}
}
