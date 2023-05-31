package com.inretailpharma.digital.ordertracker.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class TrackerReasonDto implements Serializable {
	
    private String id;
    private String reason;
}
