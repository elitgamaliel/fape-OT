package com.inretailpharma.digital.ordertracker.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class CancellationReasonDto implements Serializable {
	
    private String code;
    private String description;
}