package com.inretailpharma.digital.ordertracker.dto.external;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuditSyncDto {

	private Long ecommerceId;
	private List<AuditOrderStatusDto> statusHistory;
}
