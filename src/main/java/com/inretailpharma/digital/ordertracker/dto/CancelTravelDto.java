package com.inretailpharma.digital.ordertracker.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CancelTravelDto implements Serializable {
	
	private String motorizedId;

}
