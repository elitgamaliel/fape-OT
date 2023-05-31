package com.inretailpharma.digital.ordertracker.dto.external;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmAttendanceDto {
	
	private String confirmationUser;
	private String callBackId;
}
