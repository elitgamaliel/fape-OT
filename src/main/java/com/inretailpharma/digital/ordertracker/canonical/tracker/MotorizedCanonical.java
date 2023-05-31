package com.inretailpharma.digital.ordertracker.canonical.tracker;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MotorizedCanonical implements Serializable {

	private static final long serialVersionUID = 1L;
	private String motorizedId;
	private String status;
}
