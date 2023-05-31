package com.inretailpharma.digital.ordertracker.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShelfDto implements Serializable {
	
	private String lockCode;
	private String packCode;
}
