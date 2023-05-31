package com.inretailpharma.digital.ordertracker.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TravelDto implements Serializable {
	
	private List<String> orders;
	private String localCode;
	private String motorizedId;

}
