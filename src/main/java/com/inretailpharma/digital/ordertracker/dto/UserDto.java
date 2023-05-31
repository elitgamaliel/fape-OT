package com.inretailpharma.digital.ordertracker.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
	private String id;
	private String employeeCode;
	private String dni;
	private String email;
	private String firstName;
	private String lastName;
	private String alias;
	private Long drugstoreId;
	private List<String> roles;
	private String password;
	private String userStatus;
	private String phoneNumber;
	private Long assignedOrder;	
}
