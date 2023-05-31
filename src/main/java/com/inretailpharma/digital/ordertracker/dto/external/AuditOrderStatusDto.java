package com.inretailpharma.digital.ordertracker.dto.external;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuditOrderStatusDto {

	private Long ecommerceId;
	private String statusCode;
	private String timeFromUi;
	private String orderNote;
	private String source;
	private String updatedBy;	
	private BigDecimal latitude;
	private BigDecimal longitude;	
	private String target;
	private String statusDetail;
}
