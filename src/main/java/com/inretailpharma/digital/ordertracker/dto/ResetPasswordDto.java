package com.inretailpharma.digital.ordertracker.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResetPasswordDto implements Serializable {
	
	private String email;
	private String password;
}
