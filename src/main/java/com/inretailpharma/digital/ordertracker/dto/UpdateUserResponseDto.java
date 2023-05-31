package com.inretailpharma.digital.ordertracker.dto;

import lombok.Data;

@Data
public class UpdateUserResponseDto {
	
	private DrugstoreHeaderDto local;
	private Long creationDate;

}
